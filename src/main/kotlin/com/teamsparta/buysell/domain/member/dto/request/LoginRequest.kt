package com.teamsparta.buysell.domain.member.dto.request

import io.swagger.v3.oas.annotations.media.Schema


@Schema(description = "로그인 요청 정보")
data class LoginRequest(
    val email: String,
    val password: String,
)