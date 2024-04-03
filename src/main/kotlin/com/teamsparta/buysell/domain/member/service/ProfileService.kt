package com.teamsparta.buysell.domain.member.service

import com.teamsparta.buysell.domain.member.dto.request.MemberProfileUpdateRequest
import com.teamsparta.buysell.domain.member.dto.response.MemberResponse
import com.teamsparta.buysell.domain.member.dto.response.OtherProfileResponse
import com.teamsparta.buysell.domain.member.dto.response.ProfileResponse
import com.teamsparta.buysell.domain.order.dto.response.OrderHistoriesResponse
import com.teamsparta.buysell.domain.post.dto.response.PostListResponse
import com.teamsparta.buysell.infra.security.UserPrincipal

interface ProfileService {
    fun getAllPostByWishList(userPrincipal: UserPrincipal): List<PostListResponse>?
    fun getAllPostByMemberId(memberId:Int) : OtherProfileResponse
    fun getMyProfile(userPrincipal: UserPrincipal): MemberResponse?
    fun updateMyProfile(userPrincipal: UserPrincipal, request: MemberProfileUpdateRequest): MemberResponse
    fun getReviewsByMemberId(memberId: Int): ProfileResponse
    fun getOrderHistories(memberId: Int): List<OrderHistoriesResponse>?

}