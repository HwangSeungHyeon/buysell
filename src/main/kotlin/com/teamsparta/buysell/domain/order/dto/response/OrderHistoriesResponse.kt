package com.teamsparta.buysell.domain.order.dto.response

data class OrderHistoriesResponse(
    val imageUrl: String?,
    val postTitle: String,
    val price: Long,
    val postId: Int?
)
