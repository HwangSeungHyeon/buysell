package com.teamsparta.buysell.domain.member.service

import com.teamsparta.buysell.domain.member.dto.request.LoginRequest
import com.teamsparta.buysell.domain.member.dto.request.SignUpRequest
import com.teamsparta.buysell.infra.security.UserPrincipal

interface MemberService {
    fun signUp(request: SignUpRequest)
    fun login(request: LoginRequest): String
    fun signOut(userPrincipal: UserPrincipal)
}