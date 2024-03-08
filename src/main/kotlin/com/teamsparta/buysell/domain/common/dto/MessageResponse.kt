package com.teamsparta.buysell.domain.common.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Controller 에게 Response 로 문자열을 반환할 때 사용하는 객체.")
data class MessageResponse(
    val message: String
)
