package com.teamsparta.buysell.domain.comment.service

import com.teamsparta.buysell.domain.comment.dto.request.CreateRequest
import com.teamsparta.buysell.domain.comment.dto.request.UpdateRequest
import com.teamsparta.buysell.domain.comment.dto.response.CommentResponse

interface CommentService {
    fun addService(postId: Int, request: CreateRequest): CommentResponse

    fun editService(postId: Int, request: UpdateRequest): CommentResponse
}