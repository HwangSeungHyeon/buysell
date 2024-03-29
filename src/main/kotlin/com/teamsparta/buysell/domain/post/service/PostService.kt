package com.teamsparta.buysell.domain.post.service

import com.teamsparta.buysell.domain.common.dto.MessageResponse
import com.teamsparta.buysell.domain.post.dto.request.CreatePostRequest
import com.teamsparta.buysell.domain.post.dto.request.UpdatePostRequest
import com.teamsparta.buysell.domain.post.dto.response.PostListResponse
import com.teamsparta.buysell.domain.post.dto.response.PostResponse
import com.teamsparta.buysell.domain.post.dto.response.WishResponse
import com.teamsparta.buysell.domain.post.model.Category
import com.teamsparta.buysell.infra.security.UserPrincipal
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


interface PostService {
    fun createPost(request: CreatePostRequest, principal: UserPrincipal): MessageResponse

    fun updatePost(postId: Int, request: UpdatePostRequest, principal: UserPrincipal): MessageResponse

    fun getPosts(): List<PostResponse>

    fun getPostsWithPagination(category: Category?, pageable: Pageable): Page<PostListResponse>

    fun searchByKeyword(keyword: String, pageable: Pageable): Page<PostListResponse>

    fun getPostById(postId: Int): PostResponse

    fun deletePost(postId: Int, principal: UserPrincipal): MessageResponse

    fun getMyWishByPostId(postId: Int, userPrincipal: UserPrincipal): WishResponse

    fun addWishList(postId: Int, userPrincipal: UserPrincipal): MessageResponse

    fun cancelWishList(postId: Int, userPrincipal: UserPrincipal): MessageResponse
}