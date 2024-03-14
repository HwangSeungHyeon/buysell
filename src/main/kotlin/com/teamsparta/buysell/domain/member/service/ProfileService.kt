package com.teamsparta.buysell.domain.member.service

import com.teamsparta.buysell.domain.post.dto.response.PostResponse
import com.teamsparta.buysell.infra.security.UserPrincipal

interface ProfileService {
    fun getAllPostByMemberId(memberId:Int) : List<PostResponse>?
    fun getAllPostByLike(userPrincipal: UserPrincipal): List<PostResponse>?
}