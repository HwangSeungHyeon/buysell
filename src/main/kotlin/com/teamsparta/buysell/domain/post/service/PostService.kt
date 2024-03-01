package com.teamsparta.buysell.domain.post.service

import com.teamsparta.buysell.domain.common.dto.MessageResponse
import com.teamsparta.buysell.domain.post.dto.request.CreatePostRequest
import com.teamsparta.buysell.domain.post.dto.request.UpdatePostRequest
import com.teamsparta.buysell.domain.post.dto.response.PostListResponse
import com.teamsparta.buysell.domain.post.dto.response.PostResponse
import com.teamsparta.buysell.infra.security.UserPrincipal
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


interface PostService {
    fun createPost(request: CreatePostRequest, principal: UserPrincipal): PostResponse

    fun updatePost(postId: Int, request: UpdatePostRequest, principal: UserPrincipal): PostResponse

    fun getPosts(): List<PostResponse>

    fun searchByKeyword(keyword: String, pageable: Pageable): Page<PostListResponse>

    fun getPostById(postId: Int): PostResponse

    fun deletePost(postId: Int, principal: UserPrincipal)

    fun addLikes(postId: Int, userPrincipal: UserPrincipal): MessageResponse

    fun cancelLikes(postId: Int, userPrincipal: UserPrincipal): MessageResponse
}