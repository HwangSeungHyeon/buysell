package com.teamsparta.buysell.domain.member.service

import com.teamsparta.buysell.domain.member.repository.MemberRepository
import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class AuthLinkService(
    private val redisTemplate: RedisTemplate<String, String>,
    private val emailSender: JavaMailSender,
    @Value("\${app.baseUrl}") private val baseUrl: String,
    private val memberRepository: MemberRepository

) {
    fun sendAuthEmail(email: String){
        val baseUrl = baseUrl
        val token = UUID.randomUUID().toString()
        val url = buildAuthUrl(baseUrl, token)
        sendMessage(email, "회원가입 인증", url)

        redisTemplate.opsForValue().set(token, email, 5, TimeUnit.MINUTES)
    }
    private fun buildAuthUrl(baseUrl: String, token: String): String="$baseUrl/verify?token=$token"

    fun sendMessage(email: String, subject: String, url: String) {
        val message: MimeMessage = emailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
        helper.setTo(email)
        helper.setSubject(subject)
        helper.setText("회원가입 인증 링크:$url", true)
        emailSender.send(message)
    }

    fun verifyMember(token: String){
        val email : String = redisTemplate.opsForValue().get(token)
            ?: throw Exception("유효하지 않거나 만료된 토큰입니다.")
        val member = memberRepository.findByEmail(email)
            ?:throw Exception("사용자를 찾을 수 없습니다.",)
        member.isVerified = true
        memberRepository.save(member)

        redisTemplate.delete(token)
    }

    fun regenerateAuthLink(memberId: String): String{
        val member = memberRepository.findByIdOrNull(memberId.toInt()) ?: throw BadCredentialsException("사용자를 촺을 수 없습니다.")
        if(member.isVerified){
            throw DataIntegrityViolationException("이미 인증된 이메일이 존재합니다.")
        }
        sendAuthEmail(member.email)
        return "인증링크 재발급 성공"
    }
}