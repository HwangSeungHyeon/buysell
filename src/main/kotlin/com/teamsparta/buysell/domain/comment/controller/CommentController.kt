package com.teamsparta.buysell.domain.comment.controller

import com.teamsparta.buysell.domain.comment.dto.request.CreateRequest
import com.teamsparta.buysell.domain.comment.dto.request.UpdateRequest
import com.teamsparta.buysell.domain.comment.dto.response.CommentResponse
import com.teamsparta.buysell.domain.comment.service.CommentService
import com.teamsparta.buysell.infra.security.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RequestMapping("/posts/{postId}/comments")
@RestController
class CommentController(
    private val commentService: CommentService
) {
    @PreAuthorize("hasRole('MEMBER')")
    @Operation(summary = "댓글 작성", description = "postId를 기준으로 댓글을 작성합니다.")
    @PostMapping
    fun addComment(
        @PathVariable postId: Int,
        @RequestBody request: CreateRequest,
        @AuthenticationPrincipal principal: UserPrincipal
    ) : ResponseEntity<CommentResponse>{
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(commentService.addComment(postId, request, principal))
    }

    @PreAuthorize("hasRole('MEMBER')")
    @PutMapping("/{commentId}")
    fun editComment(
        @PathVariable postId: Int,
        @PathVariable commentId: Int,
        @RequestBody request: UpdateRequest,
        @AuthenticationPrincipal principal: UserPrincipal
    ) : ResponseEntity<CommentResponse>{
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(commentService.editComment(postId, commentId, request, principal))
    }

    @PreAuthorize("hasRole('MEMBER')")
    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @PathVariable postId: Int,
        @PathVariable commentId: Int,
        @RequestBody request: UpdateRequest,
        @AuthenticationPrincipal principal: UserPrincipal
    ) : ResponseEntity<CommentResponse>{
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(commentService.deleteComment(postId, commentId, request, principal))
    }
}