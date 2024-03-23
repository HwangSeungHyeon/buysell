package com.teamsparta.buysell.domain.coupon.dto.request

import com.teamsparta.buysell.domain.member.model.Member

data class CreateCouponRequest(
    val couponCount: Int,
    val content: String,
    val couponExp: Int
)