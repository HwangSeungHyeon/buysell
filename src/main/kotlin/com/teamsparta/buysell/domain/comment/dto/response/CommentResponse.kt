package com.teamsparta.buysell.domain.comment.dto.response

import com.teamsparta.buysell.domain.comment.model.Comment

data class CommentResponse(
    //댓글 내용
    val content: String,
    val createdName: String?
){
    companion object{
        fun toResponse(
            comment: Comment
        ):CommentResponse{
            return CommentResponse(
                content = comment.content,
                createdName = comment.createdName
            )
        }
    }
}
