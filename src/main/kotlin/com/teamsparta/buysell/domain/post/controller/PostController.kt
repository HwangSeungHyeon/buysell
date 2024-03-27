package com.teamsparta.buysell.domain.post.controller

import com.teamsparta.buysell.domain.common.dto.MessageResponse
import com.teamsparta.buysell.domain.post.dto.request.CreatePostRequest
import com.teamsparta.buysell.domain.post.dto.request.UpdatePostRequest
import com.teamsparta.buysell.domain.post.dto.response.PostListResponse
import com.teamsparta.buysell.domain.post.dto.response.PostResponse
import com.teamsparta.buysell.domain.post.dto.response.WishResponse
import com.teamsparta.buysell.domain.post.model.Category
import com.teamsparta.buysell.domain.post.model.WishList
import com.teamsparta.buysell.domain.post.service.PostService
import com.teamsparta.buysell.infra.security.UserPrincipal
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

    @PreAuthorize("hasRole('ADMIN') OR hasRole('MEMBER')") //로그인한 사람만 사용 가능 (MEMBER, ADMIN)
    @PostMapping
    fun createPost(
        @RequestBody createPostRequest: CreatePostRequest,
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<MessageResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(postService.createPost(createPostRequest, principal))
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('MEMBER')") //로그인한 사람만 사용 가능 (MEMBER, ADMIN)
    @PutMapping("/{postId}")
    fun updatePost(
        @PathVariable postId: Int,
        @RequestBody updateRequest: UpdatePostRequest,
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<MessageResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.updatePost(postId, updateRequest, principal))
    }

    @GetMapping
    fun getPosts(
        @RequestParam category: Category?,
        @PageableDefault(
            page = 0,
            size = 10,
            sort = ["title"]
        ) pageable: Pageable
    ): ResponseEntity<Page<PostListResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.getPostsWithPagination(category, pageable))
    }

    @GetMapping("/search")
    fun searchByKeyword(
        @Valid @NotBlank
        @Size(min = 1, max = 30, message = "검색어는 1자 이상 30자 이하여야 합니다.")
        @RequestParam keyword: String,
        @PageableDefault(
            page = 0,
            size = 10,
            sort = ["title"]
        ) pageable: Pageable
    ): ResponseEntity<Page<PostListResponse>>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.searchByKeyword(keyword, pageable))
    }

    @GetMapping("/{postId}")
    fun getPostById(
        @PathVariable postId: Int
    ): ResponseEntity<PostResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.getPostById(postId))
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('MEMBER')") //로그인한 사람만 사용 가능 (MEMBER, ADMIN)
    @DeleteMapping("/{postId}")
    fun deletePost(
        @PathVariable postId: Int,
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<MessageResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.deletePost(postId, principal))
    }

    @GetMapping("/{postId}/my/wishlist")
    fun getMyWishByPostId(
        @PathVariable postId: Int,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<WishResponse>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.getMyWishByPostId(postId, userPrincipal))
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('MEMBER')") //로그인한 사람만 사용 가능 (MEMBER, ADMIN)
    @PostMapping("/{postId}/my/wishlist")
    fun addWishList(
        @PathVariable postId: Int,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<MessageResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(postService.addWishList(postId, userPrincipal))
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('MEMBER')") //로그인한 사람만 사용 가능 (MEMBER, ADMIN)
    @DeleteMapping("/{postId}/my/wishlist")
    fun cancelWishList(
        @PathVariable postId: Int,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<MessageResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.cancelWishList(postId, userPrincipal))
    }
}