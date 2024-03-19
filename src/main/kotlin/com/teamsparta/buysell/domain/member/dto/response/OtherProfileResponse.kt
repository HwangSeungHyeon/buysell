package com.teamsparta.buysell.domain.member.dto.response

import com.teamsparta.buysell.domain.post.dto.response.PostResponse

data class OtherProfileResponse(
    val nickname: String?,
    val rating: Double?,
    val post: List<PostResponse>,
//    val profileUrl: String,
)
