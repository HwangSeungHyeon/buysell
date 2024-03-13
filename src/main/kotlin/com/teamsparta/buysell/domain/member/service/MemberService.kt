package com.teamsparta.buysell.domain.member.service

import com.teamsparta.buysell.domain.member.dto.request.LoginRequest
import com.teamsparta.buysell.domain.member.dto.request.MemberProfileUpdateRequest
import com.teamsparta.buysell.domain.member.dto.request.SignUpRequest
import com.teamsparta.buysell.domain.member.dto.response.MemberResponse
import com.teamsparta.buysell.domain.post.dto.response.PostResponse
import com.teamsparta.buysell.infra.security.UserPrincipal

interface MemberService {
    fun signUp(request: SignUpRequest): MemberResponse
    fun login(request: LoginRequest): String
    fun getMyProfile(userPrincipal: UserPrincipal): MemberResponse?
    fun updateMyProfile(userPrincipal: UserPrincipal, request: MemberProfileUpdateRequest): MemberResponse
    fun getAllPostByMemberId(memberId:Int) : List<PostResponse>?
    fun getAllPostByLike(userPrincipal: UserPrincipal): List<PostResponse>?
    fun signOut(userPrincipal: UserPrincipal)
    //fun googleLogin(oAuth2User: OAuth2User): JwtDto
}