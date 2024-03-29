package com.teamsparta.buysell.infra.utility

import com.teamsparta.buysell.domain.coupon.repository.CouponRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class CouponExpiration(
    private val couponRepository: CouponRepository
) {
    @Scheduled(cron = "0 0 0 * * *")
    fun deleteExpiredCoupon() {
        val expiredCoupons = couponRepository.findByCouponExp(LocalDateTime.now())
        couponRepository.deleteAll(expiredCoupons)
    }
}