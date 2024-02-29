package com.teamsparta.buysell.domain.comment.service

import com.teamsparta.buysell.domain.comment.dto.request.CreateRequest
import com.teamsparta.buysell.domain.comment.dto.request.UpdateRequest
import com.teamsparta.buysell.domain.comment.dto.response.CommentResponse
import com.teamsparta.buysell.infra.security.UserPrincipal

interface CommentService {
    fun addComment(postId: Int, request: CreateRequest, principal: UserPrincipal): CommentResponse

    fun editComment(postId: Int, commentId: Int, request: UpdateRequest, principal: UserPrincipal): CommentResponse

    fun deleteComment(postId: Int, commentId: Int, principal: UserPrincipal): CommentResponse
}