package com.teamsparta.buysell.domain.comment.controller

import com.teamsparta.buysell.domain.comment.dto.request.CreateRequest
import com.teamsparta.buysell.domain.comment.dto.request.UpdateRequest
import com.teamsparta.buysell.domain.comment.dto.response.CommentResponse
import com.teamsparta.buysell.domain.comment.service.CommentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/posts/{postId}/comments")
@RestController
class CommentController(
    private val commentService: CommentService
) {
    @PostMapping
    fun addComment(
        @PathVariable postId: String,
        @RequestBody request: CreateRequest
    ) : ResponseEntity<CommentResponse>{
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(commentService.addService(postId, request))
    }

    @PutMapping
    fun editComment(
        @PathVariable postId: String,
        @RequestBody request: UpdateRequest
    ) : ResponseEntity<CommentResponse>{
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(commentService.editComment(postId, request))
    }
}