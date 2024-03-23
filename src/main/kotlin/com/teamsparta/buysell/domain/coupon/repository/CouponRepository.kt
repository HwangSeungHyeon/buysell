package com.teamsparta.buysell.domain.coupon.repository

import com.teamsparta.buysell.domain.coupon.moedel.Coupon
import org.springframework.data.jpa.repository.JpaRepository

interface CouponRepository: JpaRepository<Coupon, Int> {
}