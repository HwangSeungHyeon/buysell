package com.teamsparta.buysell.domain.review.dto.request

data class CreateReviewRequest(
    val content: String,
    val rating: Float,
)
