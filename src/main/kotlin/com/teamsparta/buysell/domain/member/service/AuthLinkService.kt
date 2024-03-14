package com.teamsparta.buysell.domain.member.service

import com.teamsparta.buysell.domain.exception.ModelNotFoundException
import com.teamsparta.buysell.domain.member.model.Platform
import com.teamsparta.buysell.domain.member.repository.MemberRepository
import jakarta.mail.internet.MimeMessage
import jakarta.security.auth.message.AuthException
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
        val newToken = UUID.randomUUID().toString()
        val oldToken = redisTemplate.opsForValue().get(email)
        oldToken?.let{
            redisTemplate.delete(oldToken)
        }
        val url = buildAuthUrl(baseUrl, newToken)
        sendMessage(email, "회원가입 인증", url)
        redisTemplate.opsForValue().set(email, newToken, 1, TimeUnit.HOURS)
        redisTemplate.opsForValue().set(newToken, email, 1, TimeUnit.HOURS)
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
        val email : String = redisTemplate.opsForValue().get(token) ?: throw AuthException("유효하지 않거나 만료된 토큰입니다.")
        val member = memberRepository.findByEmailAndPlatform(email, platform = Platform.LOCAL)
            ?:throw ModelNotFoundException("Member", null)
        if(member.isVerified){
            throw DataIntegrityViolationException("이미 인증된 이메일이 존재합니다.")
        }
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