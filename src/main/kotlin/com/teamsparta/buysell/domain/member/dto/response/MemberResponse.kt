package com.teamsparta.buysell.domain.member.dto.response

import com.teamsparta.buysell.domain.member.model.Platform
import com.teamsparta.buysell.domain.member.model.Role
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "회원 정보 응답 데이터")
data class MemberResponse (
    val id : Int?,
    val email : String,
    val nickname: String?,
    val gender: String?,
    val birthday: String?,
)