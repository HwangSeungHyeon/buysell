package com.teamsparta.buysell.domain.member.dto.request

data class LoginRequest(
    val email: String,
    val password: String,
)