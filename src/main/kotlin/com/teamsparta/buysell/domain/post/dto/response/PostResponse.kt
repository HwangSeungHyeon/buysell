package com.teamsparta.buysell.domain.post.dto.response

import com.teamsparta.buysell.domain.comment.dto.response.CommentResponse
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "게시글을 단일 조회할 때, 페이지에 보여줄 정보를 전달하는 객체")
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
