package com.teamsparta.buysell.domain.coupon.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class AppliedUserRepository(
    private val redisTemplate: RedisTemplate<String, String>
) {
    fun add(userId: Int): Long {
        return redisTemplate
            .opsForSet()
            .add("applied_user", userId.toString())
            ?: 0
    }
}