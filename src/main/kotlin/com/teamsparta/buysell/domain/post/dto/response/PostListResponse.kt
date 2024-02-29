package com.teamsparta.buysell.domain.post.dto.response

data class PostListResponse(
    val id: Int,
    val title: String,
    val createdName: String,
    val price: Int
)
