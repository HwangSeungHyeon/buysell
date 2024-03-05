package com.teamsparta.buysell.domain.exception.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "에러가 발생했을 때 에러 문구를 전달하는 객체.")
data class ErrorResponse(val message: String?)
