package com.teamsparta.buysell.domain.member.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.util.concurrent.TimeUnit

@Service
class AuthCodeService(
    private val redisTemplate: RedisTemplate<String, String>

) {
    fun generateAndSaveAuthCode(memberId: String, expirationTime: Long): String {
        val authCode = generateSecureAuthCode()
        redisTemplate.opsForValue().set(buildAuthCodeKey(memberId), authCode, expirationTime, TimeUnit.SECONDS)
        return authCode
    }
    fun generateSecureAuthCode(): String {
        val secureRandom = SecureRandom()
        val number = secureRandom.nextInt(900000) + 100000
        return number.toString()
    }
    fun validateAuthCode(memberId: String, authCode: String): Boolean {
        val key = buildAuthCodeKey(memberId)
        val storedAuthCode = redisTemplate.opsForValue().get(key)
        return authCode == storedAuthCode
    }

    fun deleteAuthCode(memberId: String) {
        redisTemplate.delete(buildAuthCodeKey(memberId))
    }

    private fun buildAuthCodeKey(memberId: String): String = "authCode:$memberId"

}