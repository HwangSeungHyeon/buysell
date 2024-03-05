package com.teamsparta.buysell.domain.post.dto.response

import com.teamsparta.buysell.domain.comment.dto.response.CommentResponse

data class PostResponse(
    val id: Int,
    val title: String,
    val content: String,
    val createdName: String?,
    val price: Int,
    val isSoldout: Boolean,
    val comment: List<CommentResponse>
//    val imgUrl: List<blahbalh>
//    val category: Category
)
