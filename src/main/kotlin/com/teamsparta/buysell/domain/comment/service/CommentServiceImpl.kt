package com.teamsparta.buysell.domain.comment.service

import com.teamsparta.buysell.domain.comment.dto.request.CreateRequest
import com.teamsparta.buysell.domain.comment.dto.response.CommentResponse
import com.teamsparta.buysell.domain.comment.model.Comment
import com.teamsparta.buysell.domain.comment.repository.CommentRepository
import org.springframework.stereotype.Service

@Service
class CommentServiceImpl(
    private val commentRepository: CommentRepository
) :CommentService {
    override fun addService(
        postId: Int,
        request: CreateRequest
    ) : CommentResponse {


        Comment.makeEntity(request).let { commentRepository.save(it) }

        return CommentResponse("댓글이 작성되었습니다.")
    }
}