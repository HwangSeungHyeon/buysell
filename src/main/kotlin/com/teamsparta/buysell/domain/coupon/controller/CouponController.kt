package com.teamsparta.buysell.domain.coupon.controller

import com.teamsparta.buysell.domain.common.dto.MessageResponse
import com.teamsparta.buysell.domain.coupon.dto.request.CreateCouponRequest
import com.teamsparta.buysell.domain.coupon.service.CouponService
import com.teamsparta.buysell.infra.security.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/coupons")
class CouponController(
    private val couponService: CouponService
) {
    @PostMapping("/creates")
    fun createCoupon(
        @AuthenticationPrincipal principal: UserPrincipal,
        @RequestBody request: CreateCouponRequest
    ): ResponseEntity<MessageResponse>{
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(couponService.createCoupon(request, principal.id))
    }
}