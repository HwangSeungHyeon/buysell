package com.teamsparta.buysell.domain.coupon.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class RedisCouponRepository(
    private val redisTemplate: RedisTemplate<String, String>
) {
    fun increment() : Long {
        return redisTemplate
            .opsForValue()
            .increment("coupon_count")
            ?: throw IllegalStateException("Failed to increment")
    }
}