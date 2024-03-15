package com.teamsparta.buysell.domain.member.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Pattern

data class MemberProfileUpdateRequest(
/*    val email : String,*/
    @field:Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).{2,8}$",
        message = "닉네임은 숫자, 영어 대소문자를 포함한 최소 2자 최대 8자 이어야 합니다.")
    @Schema(description = "수정할 닉네임", example = "test1234")
    val nickname: String?,

    @field:Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "생년월일은 'yyyy-mm-dd' 형식으로 입력해주세요.")
    val birthday: String?
)
