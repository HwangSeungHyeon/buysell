package com.teamsparta.buysell.domain.post.dto.request

import com.teamsparta.buysell.domain.post.model.Category

data class CreatePostRequest(
    val title: String,
    val content: String,
    val price: Int,
    val category: Category
//    val imgUrl:
)
