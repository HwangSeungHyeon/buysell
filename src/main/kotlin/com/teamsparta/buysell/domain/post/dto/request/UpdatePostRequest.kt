package com.teamsparta.buysell.domain.post.dto.request

data class UpdatePostRequest(
    val title: String,
    val content: String,
    val price: Int,
//    val category: Category
)