package com.teamsparta.buysell.domain.member.dto.response

import com.teamsparta.buysell.domain.review.dto.response.ReviewResponse

data class ProfileResponse(
    val nickname: String?,
    val review: List<ReviewResponse>
)