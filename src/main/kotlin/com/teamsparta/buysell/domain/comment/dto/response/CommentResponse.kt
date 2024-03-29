package com.teamsparta.buysell.domain.comment.dto.response

import com.teamsparta.buysell.domain.comment.model.Comment

data class CommentResponse(
    //댓글 내용
    val id: Int?,
    val content: String,
    val createdName: String?,
    val memberId: Int?,
){
    companion object{
        fun toResponse(
            comment: Comment
        ):CommentResponse{
            return CommentResponse(
                id = comment.id,
                content = comment.content,
                createdName = comment.member.nickname,
                memberId = comment.member.id,
            )
        }
    }
}
