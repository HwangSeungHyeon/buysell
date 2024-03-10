package com.teamsparta.buysell.domain.member.dto.request

data class VerifyRequest(
    val memberId: String,
    val inputVerificationCode: String
)