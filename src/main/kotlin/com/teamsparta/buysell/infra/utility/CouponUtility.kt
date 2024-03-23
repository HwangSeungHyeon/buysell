package com.teamsparta.buysell.infra.utility

import org.springframework.stereotype.Component

@Component
class CouponUtility {

    fun createCouponNumber(): String {
        val length = 4
        val charset = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"

        val coupon1depth = (1..length).map {charset.random()}.joinToString("")
        val coupon2depth = (1..length).map {charset.random()}.joinToString("")
        val coupon3depth = (1..length).map {charset.random()}.joinToString("")
        val coupon4depth = (1..length).map {charset.random()}.joinToString("")

        val coupon = coupon1depth + coupon2depth + coupon3depth +coupon4depth

        return coupon
    }
}