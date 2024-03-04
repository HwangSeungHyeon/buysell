package com.teamsparta.buysell.domain.post.controller

import com.teamsparta.buysell.domain.common.dto.MessageResponse
import com.teamsparta.buysell.domain.post.dto.request.CreatePostRequest
import com.teamsparta.buysell.domain.post.dto.request.UpdatePostRequest
import com.teamsparta.buysell.domain.post.dto.response.PostListResponse
import com.teamsparta.buysell.domain.post.dto.response.PostResponse
import com.teamsparta.buysell.domain.post.model.Category
import com.teamsparta.buysell.domain.post.service.PostService
import com.teamsparta.buysell.infra.security.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
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

    @PreAuthorize("hasRole('MEMBER')")
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
    fun getPosts(
        @PageableDefault(
            page = 0,
            size = 10,
            sort = ["title"]
        ) pageable: Pageable
    ): ResponseEntity<Page<PostListResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.getPostsWithPagination(pageable))
    }

    @GetMapping("/{postId}")
    fun getPostById(
        @PathVariable postId: Int
    ): ResponseEntity<PostResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.getPostById(postId))
    }

    @PreAuthorize("hasRole('MEMBER')")
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

    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping("/{postId}/likes")
    fun addLikes(
        @PathVariable postId: Int,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ) : ResponseEntity<MessageResponse>{
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(postService.addLikes(postId, userPrincipal))
    }

    @PreAuthorize("hasRole('MEMBER')")
    @DeleteMapping("/{postId}/likes")
    fun cancelLikes(
        @PathVariable postId: Int,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ) : ResponseEntity<MessageResponse>{
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(postService.cancelLikes(postId, userPrincipal))
    }

    @GetMapping("/categories")
    fun getCategoryList(): ResponseEntity<List<String>>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(Category.entries.map { it.label })
    }
}