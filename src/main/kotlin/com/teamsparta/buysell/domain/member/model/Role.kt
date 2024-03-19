package com.teamsparta.buysell.domain.member.model

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "회원 유형")
enum class Role {
    MEMBER,
    ADMIN,
    FREEZE
}