package com.teamsparta.buysell.domain.member.model

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "플랫폼 유형")
enum class Platform {
    GOOGLE,
    NAVER,
    KAKAO,
    LOCAL
}