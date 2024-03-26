package com.teamsparta.buysell.domain.member.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Pattern

data class MemberProfileUpdateRequest(
/*    val email : String,*/
    @field:Pattern(
        regexp = "^[\\w가-힣]{2,8}$",
        message = "닉네임은 숫자, 영어 대소문자, 한글을 포함한 최소 2자 최대 8자 이어야 하며, 중복, 공백은 허용하지 않습니다.")
    @Schema(description = "회원가입에 사용할 닉네임", example = "test123")
    val nickname: String,

    @field:Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "생년월일은 'yyyy-mm-dd' 형식으로 입력해주세요.")
    val birthday: String
)
