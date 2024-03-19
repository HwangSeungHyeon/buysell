package com.teamsparta.buysell.domain.comment.controller

import com.teamsparta.buysell.domain.comment.dto.request.CreateRequest
import com.teamsparta.buysell.domain.comment.dto.request.UpdateRequest
import com.teamsparta.buysell.domain.comment.service.CommentService
import com.teamsparta.buysell.domain.common.dto.MessageResponse
import com.teamsparta.buysell.infra.security.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
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
    @PreAuthorize("hasRole('ADMIN') OR hasRole('MEMBER')") //로그인한 사람만 사용 가능 (MEMBER, ADMIN)
    @Operation(summary = "댓글 작성", description = "댓글을 작성합니다.")
    @PostMapping
    fun addComment(
        @PathVariable postId: Int,
        @Valid @RequestBody request: CreateRequest,
        @AuthenticationPrincipal principal: UserPrincipal
    ) : ResponseEntity<MessageResponse>{
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(commentService.addComment(postId, request, principal))
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('MEMBER')") //로그인한 사람만 사용 가능 (MEMBER, ADMIN)
    @Operation(summary = "댓글 수정", description = "작성한 댓글을 수정합니다.")
    @PutMapping("/{commentId}")
    fun editComment(
        @PathVariable postId: Int,
        @PathVariable commentId: Int,
        @Valid @RequestBody request: UpdateRequest,
        @AuthenticationPrincipal principal: UserPrincipal
    ) : ResponseEntity<MessageResponse>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(commentService.editComment(postId, commentId, request, principal))
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('MEMBER')") //로그인한 사람만 사용 가능 (MEMBER, ADMIN)
    @Operation(summary = "comment 삭제", description = "작성한 댓글을 SoftDelete 합니다.")
    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @PathVariable postId: Int,
        @PathVariable commentId: Int,
        @AuthenticationPrincipal principal: UserPrincipal
    ) : ResponseEntity<MessageResponse>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(commentService.deleteComment(postId, commentId, principal))
    }
}