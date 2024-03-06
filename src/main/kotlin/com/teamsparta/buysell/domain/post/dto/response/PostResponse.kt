package com.teamsparta.buysell.domain.post.dto.response

data class PostResponse(
    val id: Int,
    val title: String,
    val content: String,
    val createdName: String?,
    val price: Int,
    val isSoldout: Boolean
//    val imgUrl: List<blahbalh>
//    val category: Category
)
