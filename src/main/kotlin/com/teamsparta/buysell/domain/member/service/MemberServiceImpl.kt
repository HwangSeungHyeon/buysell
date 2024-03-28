package com.teamsparta.buysell.domain.member.service

import com.teamsparta.buysell.domain.exception.ModelNotFoundException
import com.teamsparta.buysell.domain.member.dto.request.LoginRequest
import com.teamsparta.buysell.domain.member.dto.request.SignUpRequest
import com.teamsparta.buysell.domain.member.model.Account
import com.teamsparta.buysell.domain.member.model.Member
import com.teamsparta.buysell.domain.member.model.Platform
import com.teamsparta.buysell.domain.member.model.Role
import com.teamsparta.buysell.domain.member.repository.MemberRepository
import com.teamsparta.buysell.infra.security.UserPrincipal
import com.teamsparta.buysell.infra.security.jwt.JwtPlugin
import jakarta.transaction.Transactional
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin,
    private val authLinkService: AuthLinkService,
): MemberService{
    override fun signUp(request: SignUpRequest){
        if(memberRepository.existsByEmailAndPlatform(request.email, Platform.LOCAL)){
            throw DataIntegrityViolationException("이미 가입된 계정입니다.")
        }

        if (memberRepository.existsByNickname(request.nickname))
            throw DataIntegrityViolationException("이미 사용 중인 닉네임 입니다.")

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
        authLinkService.sendAuthEmail(member.email)
    }

    override fun login(request: LoginRequest): String {
        val member = memberRepository.findByEmailAndPlatform(request.email, Platform.LOCAL)
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

    //회원 탈퇴 요청
    @Transactional
    override fun signOut(userPrincipal: UserPrincipal) {
        val member = memberInformation(userPrincipal)
        member.role = Role.FREEZE
        memberRepository.delete(member)
    }

    private fun memberInformation(userPrincipal: UserPrincipal) = memberRepository.findByIdOrNull(userPrincipal.id)
        ?:throw ModelNotFoundException("member",userPrincipal.id)
}
