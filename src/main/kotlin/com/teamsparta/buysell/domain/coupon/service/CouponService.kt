package com.teamsparta.buysell.domain.coupon.service

import com.teamsparta.buysell.domain.common.dto.MessageResponse
import com.teamsparta.buysell.domain.coupon.dto.request.CreateCouponRequest
import com.teamsparta.buysell.infra.security.UserPrincipal
import org.apache.catalina.User

interface CouponService {
    fun createCoupon(request: CreateCouponRequest, memberId: Int): MessageResponse
}