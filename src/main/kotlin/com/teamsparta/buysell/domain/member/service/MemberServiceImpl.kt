package com.teamsparta.buysell.domain.member.service

import com.teamsparta.buysell.domain.member.dto.request.LoginRequest
import com.teamsparta.buysell.domain.member.dto.request.SignUpRequest
import com.teamsparta.buysell.domain.member.dto.response.MemberResponse
import com.teamsparta.buysell.domain.member.model.Member
import com.teamsparta.buysell.domain.member.model.Platform
import com.teamsparta.buysell.domain.member.model.Role
import com.teamsparta.buysell.domain.member.repository.MemberRepository
import com.teamsparta.buysell.infra.security.jwt.JwtPlugin
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin,
): MemberService {
    override fun signUp(request: SignUpRequest): MemberResponse {
        memberRepository.findByEmail(request.email)?.let {
            throw DataIntegrityViolationException("이미 존재하는 이메일입니다.")
        }
        val member = Member(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            nickname = request.nickname,
            role = Role.MEMBER,
            gender = request.gender,
            birthday = request.birthday,
            platform = Platform.LOCAL,
        )
        memberRepository.save(member)
        return member.toResponse()
    }

    override fun login(request: LoginRequest): String {
        val member = memberRepository.findByEmail(request.email)
            ?: throw BadCredentialsException("이메일이 존재하지 않거나 틀렸습니다.")
        if(!passwordEncoder.matches(request.password,member.password)){
            throw BadCredentialsException("비밀번호가 존재하지 않거나 틀렸습니다.")
        }
        val token = jwtPlugin.generateAccessToken(member.id.toString(), member.email, member.role.toString(), member.platform.toString())
        return token
    }



}