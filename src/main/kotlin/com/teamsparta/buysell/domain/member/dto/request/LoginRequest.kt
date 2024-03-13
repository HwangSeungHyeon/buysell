package com.teamsparta.buysell.domain.member.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern


@Schema(description = "로그인 요청 정보")
data class LoginRequest(
    @field:Email(regexp = "\\w+@\\w+\\.\\w+(\\.\\w+)?", message = "이메일 형식이 옳바르지 않습니다.")
    @Schema(description = "회원가입에 사용할 이메일", example = "test@gmail.com")
    val email: String,

    @field:Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W).{6,12}$",
        message = "비밀번호는 숫자, 대소문자, 특수문자를 포함한 6자 이상 12자 이하입니다.")
    @Schema(description = "회원가입에 사용할 비밀번호", example = "evy123!")
    val password: String,
)