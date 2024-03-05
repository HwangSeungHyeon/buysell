package com.teamsparta.buysell.domain.member.dto.response

import com.teamsparta.buysell.domain.member.model.Platform
import com.teamsparta.buysell.domain.member.model.Role

data class MemberResponse (
    val id : Int?,
    val email : String,
    val nickname: String?,
    val role: Role?,
    val gender: String?,
    val birthday: String?,
    val platform: Platform?,
)