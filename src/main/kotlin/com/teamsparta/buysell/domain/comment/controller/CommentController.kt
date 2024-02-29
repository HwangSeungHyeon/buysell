package com.teamsparta.buysell.domain.comment.controller

import com.teamsparta.buysell.domain.comment.dto.request.CreateRequest
import com.teamsparta.buysell.domain.comment.dto.request.UpdateRequest
import com.teamsparta.buysell.domain.comment.dto.response.CommentResponse
import com.teamsparta.buysell.domain.comment.service.CommentService
import com.teamsparta.buysell.infra.security.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@Tag(name = "comments", description = "댓글 API")
@RequestMapping("/posts/{postId}/comments")
@RestController
class CommentController(
    private val commentService: CommentService
) {
    @PreAuthorize("hasRole('MEMBER')")
    @Operation(summary = "댓글 작성", description = "댓글을 작성합니다.")
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
    @Operation(summary = "댓글 수정", description = "작성한 댓글을 수정합니다.")
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
    @Operation(summary = "comment 삭제")
    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @PathVariable postId: Int,
        @PathVariable commentId: Int,
        @AuthenticationPrincipal principal: UserPrincipal
    ) : ResponseEntity<CommentResponse>{
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(commentService.deleteComment(postId, commentId, principal))
    }
}