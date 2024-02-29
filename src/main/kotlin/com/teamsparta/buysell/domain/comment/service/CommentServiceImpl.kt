package com.teamsparta.buysell.domain.comment.service

import com.teamsparta.buysell.domain.comment.dto.request.CreateRequest
import com.teamsparta.buysell.domain.comment.dto.request.UpdateRequest
import com.teamsparta.buysell.domain.comment.dto.response.CommentResponse
import com.teamsparta.buysell.domain.comment.model.Comment
import com.teamsparta.buysell.domain.comment.repository.CommentRepository
import com.teamsparta.buysell.domain.exception.ForbiddenException
import com.teamsparta.buysell.domain.exception.ModelNotFoundException
import com.teamsparta.buysell.domain.member.repository.MemberRepository
import com.teamsparta.buysell.domain.post.repository.PostRepository
import com.teamsparta.buysell.infra.security.UserPrincipal
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
    private val memberRepository: MemberRepository
) :CommentService {
    override fun addComment(
        postId: Int,
        request: CreateRequest,
        principal: UserPrincipal
    ) : CommentResponse {

        val member = memberRepository.findByIdOrNull(principal.id)
            ?:throw ModelNotFoundException("Member", principal.id)

        val post = postRepository.findByIdOrNull(postId)
            ?:throw ModelNotFoundException("Post", postId)

        Comment.makeEntity(
            request = request,
            member = member,
            post = post
        ).let { commentRepository.save(it) }

        return CommentResponse("댓글이 작성되었습니다.")
    }

    @Transactional
    override fun editComment(
        postId: Int,
        commentId: Int,
        request: UpdateRequest,
        principal: UserPrincipal
    ): CommentResponse {
        val comment = commentRepository
            .findByPostIdAndId(postId, commentId)

        Comment.checkPermission(comment, principal)

        comment.edit(request)

        return CommentResponse("댓글이 수정되었습니다.")
    }
}