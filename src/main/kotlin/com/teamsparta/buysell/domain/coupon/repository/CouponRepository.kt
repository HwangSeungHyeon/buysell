package com.teamsparta.buysell.domain.coupon.repository

import com.teamsparta.buysell.domain.coupon.moedel.Coupon
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface CouponRepository: JpaRepository<Coupon, Int> {
    fun findByCouponExp(now: LocalDateTime): List<Coupon>
}
