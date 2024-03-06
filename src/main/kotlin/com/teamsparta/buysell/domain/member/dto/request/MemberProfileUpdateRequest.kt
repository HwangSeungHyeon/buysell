package com.teamsparta.buysell.domain.member.dto.request

data class MemberProfileUpdateRequest(
    val email : String,
    val nickname: String?,
    val gender: String?,
    val birthday: String?
)