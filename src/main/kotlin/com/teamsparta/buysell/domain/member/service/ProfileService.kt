package com.teamsparta.buysell.domain.member.service

import com.teamsparta.buysell.domain.member.dto.request.MemberProfileUpdateRequest
import com.teamsparta.buysell.domain.member.dto.response.MemberResponse
import com.teamsparta.buysell.domain.post.dto.response.PostResponse
import com.teamsparta.buysell.infra.security.UserPrincipal

interface ProfileService {
    fun getAllPostByMemberId(memberId:Int) : List<PostListResponse>?
    fun getAllPostByLike(userPrincipal: UserPrincipal): List<PostListResponse>?
    fun getAllPostByMemberId(memberId:Int) : OtherProfileResponse
    fun getAllPostByLike(userPrincipal: UserPrincipal): List<PostResponse>?
    fun getMyProfile(userPrincipal: UserPrincipal): MemberResponse?
    fun updateMyProfile(userPrincipal: UserPrincipal, request: MemberProfileUpdateRequest): MemberResponse
    fun getReviewsByMemberId(memberId: Int): List<ReviewResponse>
}