package com.teamsparta.buysell.domain.member.dto.response

import com.teamsparta.buysell.domain.member.model.Platform
import com.teamsparta.buysell.domain.member.model.Role

data class SocialResponse (
    val id : Int?,
    val email : String?,
    val role: Role?,
    val platform: Platform?,
)