package com.teamsparta.buysell.domain.member.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length

data class SignUpRequest (
    @field:Email(regexp = "\\w+@\\w+\\.\\w+(\\.\\w+)?", message = "이메일 형식이 옳바르지 않습니다.")
    val email: String,

    @field:Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W).{6,12}$",
        message = "비밀번호는 숫자, 대소문자, 특수문자를 포함한 6자 이상 12자 이하로 설정해주세요.")
    val password: String?,

    @field:Length(max= 8, message = "닉네임은 최대 8자 이어야 합니다.")
    val nickname: String?,

    @field:Pattern(regexp = "^(남자|여자)$", message = "성별은 '남자' 또는 '여자' 중에서 선택해주세요.")
    val gender: String?,

    @field:Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "생년월일은 'yyyy-mm-dd' 형식으로 입력해주세요.")
    val birthday: String?,
)