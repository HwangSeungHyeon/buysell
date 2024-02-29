package com.teamsparta.buysell.domain.post.controller

import com.teamsparta.buysell.domain.post.dto.request.CreatePostRequest
import com.teamsparta.buysell.domain.post.dto.request.UpdatePostRequest
import com.teamsparta.buysell.domain.post.dto.response.PostResponse
import com.teamsparta.buysell.domain.post.service.PostService
import com.teamsparta.buysell.infra.security.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RequestMapping("/posts")
@RestController
class PostController(
    private val postService: PostService
) {

    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping
    fun createPost(
        @RequestBody createPostRequest: CreatePostRequest,
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<PostResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(postService.createPost(createPostRequest, principal))
    }

    @PutMapping("/{postId}")
    fun updatePost(
        @PathVariable postId: Int,
        @RequestBody updateRequest: UpdatePostRequest,
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<PostResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.updatePost(postId, updateRequest, principal))
    }

    @GetMapping
    fun getPosts(): ResponseEntity<List<PostResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.getPosts())
    }

    @GetMapping("/{postId}")
    fun getPostById(
        @PathVariable postId: Int
    ): ResponseEntity<PostResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.getPostById(postId))
    }

    @DeleteMapping("/{postId}")
    fun deletePost(
        @PathVariable postId: Int,
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<Unit> {
        postService.deletePost(postId, principal)
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build()
    }
}