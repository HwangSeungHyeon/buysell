package com.teamsparta.buysell.domain.post.dto.response

import com.teamsparta.buysell.domain.comment.dto.response.CommentResponse
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "게시글을 단일 조회할 때, 페이지에 보여줄 정보를 전달하는 객체")
data class PostResponse(

    val id: Int,
    val memberId: Int,
    val title: String,
    val content: String,
    val createdName: String?,
    val price: Long,
    val isSoldout: Boolean,
    val comment: List<CommentResponse>,
    val view: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val imageUrl: String?
)
