package com.teamsparta.buysell.domain.coupon.dto.request

data class CreateCouponRequest(
    val couponCount: Int,
    val content: String,
    val couponExp: Long
)