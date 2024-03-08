package com.teamsparta.buysell.domain.comment.service

import com.teamsparta.buysell.domain.comment.dto.request.CreateRequest
import com.teamsparta.buysell.domain.comment.dto.request.UpdateRequest
import com.teamsparta.buysell.domain.common.dto.MessageResponse
import com.teamsparta.buysell.infra.security.UserPrincipal

interface CommentService {
    fun addComment(postId: Int, request: CreateRequest, principal: UserPrincipal): MessageResponse

    fun editComment(postId: Int, commentId: Int, request: UpdateRequest, principal: UserPrincipal): MessageResponse

    fun deleteComment(postId: Int, commentId: Int, principal: UserPrincipal): MessageResponse
}