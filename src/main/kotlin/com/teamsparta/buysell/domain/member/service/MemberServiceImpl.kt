package com.teamsparta.buysell.domain.member.service

import com.teamsparta.buysell.domain.exception.ModelNotFoundException
import com.teamsparta.buysell.domain.member.dto.request.LoginRequest
import com.teamsparta.buysell.domain.member.dto.request.MemberProfileUpdateRequest
import com.teamsparta.buysell.domain.member.dto.request.SignUpRequest
import com.teamsparta.buysell.domain.member.dto.response.MemberResponse
import com.teamsparta.buysell.domain.member.model.*
import com.teamsparta.buysell.domain.member.repository.MemberRepository
import com.teamsparta.buysell.domain.post.dto.response.PostResponse
import com.teamsparta.buysell.domain.post.model.toResponse
import com.teamsparta.buysell.domain.post.repository.LikeRepository
import com.teamsparta.buysell.domain.post.repository.PostRepository
import com.teamsparta.buysell.infra.security.UserPrincipal
import com.teamsparta.buysell.infra.security.jwt.JwtPlugin
import jakarta.security.auth.message.AuthException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val postRepository: PostRepository,
    private val likeRepository: LikeRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin,
    private val authCodeService: AuthCodeService,
    private val emailService: EmailService,

): MemberService{
    override fun signUp(request: SignUpRequest): MemberResponse {
        request.nickname?.let { nickname ->
            memberRepository.findByNickname(nickname)?.also {
                throw DataIntegrityViolationException("이미 사용 중인 닉네임 입니다.")
            }
        }
        memberRepository.findByEmail(request.email)?.let { existingMember ->
            if (!existingMember.isVerified || existingMember.isDeleted) {
                existingMember.apply {
                    isDeleted = false
                    email = request.email
                    password = passwordEncoder.encode(request.password)
                    nickname = request.nickname
                    birthday = request.birthday
                    gender = request.gender
                }
                memberRepository.save(existingMember)
                val authCode = authCodeService.generateAndSaveAuthCode(existingMember.id.toString(), 300)
                val messageText = "회원가입을 완료하려면 다음의 인증 코드를 입력해주세요: $authCode"
                emailService.sendMessage(existingMember.email, "회원가입 인증 코드", messageText)
                return existingMember.toResponse()
            } else {
                throw DataIntegrityViolationException("이미 인증된 이메일이 존재합니다.")
            }
        }


        val member = Member(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            nickname = request.nickname,
            role = Role.MEMBER,
            gender = request.gender,
            birthday = request.birthday,
            platform = Platform.LOCAL,
            account = Account(),
            isVerified = false
        )
        memberRepository.save(member)
        val authCode = authCodeService.generateAndSaveAuthCode(member.id.toString(), 300)
        val messageText = "회원가입을 완료하려면 다음의 인증 코드를 입력해주세요: $authCode"
        emailService.sendMessage(member.email, "회원가입 인증 코드", messageText)
        return member.toResponse()
    }
    override fun regenerateAuthCode(memberId: String): String{
            authCodeService.deleteAuthCode(memberId)
            val member = memberRepository.findByIdOrNull(memberId.toInt()) ?: throw BadCredentialsException("사용자를 촺을 수 없습니다.")
            val newAuthCode = authCodeService.generateAndSaveAuthCode(member.id.toString(), 300)
            val messageText = "인증 코드를 재발급 받았습니다: $newAuthCode"
            emailService.sendMessage(member.email, "회원가입 인증 코드", messageText)
            return "인증코드 재발급 성공"
    }

    override fun verifyMember(memberId: String, inputVerificationCode: String): String {
            if (!authCodeService.validateAuthCode(memberId, inputVerificationCode)) {
                throw AuthException("유효하지 않은 인증코드 입니다.")
            }
            val memberInt = memberId.toIntOrNull() ?: throw ModelNotFoundException("Member",null)
            val member = memberRepository.findById(memberInt)
                .orElseThrow {ModelNotFoundException("Member",memberInt)}

            member.isVerified = true
            memberRepository.save(member)
            authCodeService.deleteAuthCode(memberId)
            return "회원 인증 성공"
    }

    override fun login(request: LoginRequest): String {
        val member = memberRepository.findByEmail(request.email)
            ?: throw BadCredentialsException("이메일이나 비밀번호가 존재하지않거나 틀렸습니다.")
        if(!passwordEncoder.matches(request.password,member.password)){
            throw BadCredentialsException("이메일이나 비밀번호가 존재하지않거나 틀렸습니다.")
        }
        if (!member.isVerified) {
            throw BadCredentialsException("계정이 인증되지 않았습니다. 이메일을 확인해주세요.")
        }
        val token = jwtPlugin.generateAccessToken(member.id.toString(), member.email, member.role.toString(), member.platform.toString())
        return token
    }

    @Transactional
    override fun getMember(userPrincipal: UserPrincipal): MemberResponse? {
        val member = memberRepository.findByIdOrNull(userPrincipal.id)
            ?: throw ModelNotFoundException("Member", userPrincipal.id)
        return member.toResponse()
    } // 멤버 아이디 기준 정보 조회

    @Transactional
    override fun updateMember(userPrincipal: UserPrincipal, request: MemberProfileUpdateRequest): MemberResponse {
        val member = memberRepository.findByIdOrNull(userPrincipal.id)
            ?: throw ModelNotFoundException("Member", userPrincipal.id)
        member.nickname = request.nickname
        member.birthday = request.birthday
       return memberRepository.save(member).toResponse()
    } // 멤버 아이디 기준 정보 수정

    override fun getAllPostByUserPrincipal(userPrincipal: UserPrincipal): List<PostResponse>? {
        val member = memberRepository.findByIdOrNull(userPrincipal.id)
            ?: throw ModelNotFoundException("member", userPrincipal.id)
        val post = postRepository.findAllByMember(member)
        return post.map { it.toResponse() }
    }//내가 쓴 글 전체 조회

    override fun getAllPostByLike(userPrincipal: UserPrincipal): List<PostResponse>? {
        val member = memberRepository.findByIdOrNull(userPrincipal.id)
            ?: throw ModelNotFoundException("member", userPrincipal.id)
        val like = likeRepository.findByMember(member)
        val post = like.map { it.post }
        return post.map { it.toResponse() }
    }//내가 찜 한 글 전체 조회

    override fun pretendDelete(userPrincipal: UserPrincipal) {
        val member = memberInformation(userPrincipal)

        memberRepository.delete(member)
    } //회원 탈퇴 요청

    private fun memberInformation(userPrincipal: UserPrincipal) = memberRepository.findByIdOrNull(userPrincipal.id)
        ?:throw ModelNotFoundException("member",userPrincipal.id)
}
