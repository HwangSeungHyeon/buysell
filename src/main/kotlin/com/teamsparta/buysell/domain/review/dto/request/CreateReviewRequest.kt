package com.teamsparta.buysell.domain.review.dto.request

data class CreateReviewRequest(
    val postId: Int,
    val content: String,
    val rating: Float,
)
