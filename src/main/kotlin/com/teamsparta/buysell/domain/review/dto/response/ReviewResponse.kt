package com.teamsparta.buysell.domain.review.dto.response

data class ReviewResponse(
    val id: Int?,
    val createdName: String,
    val rating: Float,
    val content: String,
)
