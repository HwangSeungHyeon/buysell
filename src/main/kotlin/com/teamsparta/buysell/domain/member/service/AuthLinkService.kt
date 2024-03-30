package com.teamsparta.buysell.domain.member.service

import com.teamsparta.buysell.domain.common.dto.MessageResponse
import com.teamsparta.buysell.domain.exception.ModelNotFoundException
import com.teamsparta.buysell.domain.member.model.Platform
import com.teamsparta.buysell.domain.member.repository.MemberRepository
import jakarta.mail.internet.MimeMessage
import jakarta.security.auth.message.AuthException
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class AuthLinkService(
    private val redisTemplate: RedisTemplate<String, String>,
    private val emailSender: JavaMailSender,
    @Value("\${app.serverUrl}") private val serverUrl: String,
    private val memberRepository: MemberRepository,
) {
    fun sendAuthEmail(email: String): MessageResponse{
        redisTemplate.delete(email)
        val serverUrl = serverUrl
        val newToken = UUID.randomUUID().toString()
        val url = buildAuthUrl(serverUrl, email, newToken)
        sendMessage(email, "회원가입 인증", url)
        redisTemplate.opsForValue().set(email, newToken, 1, TimeUnit.HOURS)
        return MessageResponse("이메일로 인증코드를 발송하였습니다.")
    }

    private fun buildAuthUrl(serverUrl: String, email: String, token: String): String="$serverUrl/members/verify?email=$email&token=$token"

    fun sendMessage(email: String, subject: String, url: String) {
        val message: MimeMessage = emailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
        helper.setTo(email)
        helper.setSubject(subject)
        helper.setText("회원가입 인증 링크: $url", true)
        emailSender.send(message)
    }

    fun verifyMember(email:String, token: String): MessageResponse{
        val redisToken = redisTemplate.opsForValue().get(email)
        if(redisToken != token){
            throw AuthException("유효하지 않거나 만료된 토큰입니다.")
        }
        val member = memberRepository.findByEmailAndPlatform(email, platform = Platform.LOCAL)
            ?:throw ModelNotFoundException("Member", null)
        if(member.isVerified){
            throw DataIntegrityViolationException("이미 인증된 이메일이 존재합니다.")
        }
        member.isVerified = true
        memberRepository.save(member)

        redisTemplate.delete(email)
        return MessageResponse("이메일 인증이 완료되었습니다.")
    }
}