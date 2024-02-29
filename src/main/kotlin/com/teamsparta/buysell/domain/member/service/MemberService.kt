package com.teamsparta.buysell.domain.member.service

import com.teamsparta.buysell.domain.member.dto.request.SignUpRequest
import com.teamsparta.buysell.domain.member.dto.response.MemberResponse

interface MemberService {
    fun signUp(request: SignUpRequest): MemberResponse

}