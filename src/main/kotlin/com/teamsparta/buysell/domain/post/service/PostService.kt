package com.teamsparta.buysell.domain.post.service

import com.teamsparta.buysell.domain.post.dto.request.CreatePostRequest
import com.teamsparta.buysell.domain.post.dto.request.UpdatePostRequest
import com.teamsparta.buysell.domain.post.dto.response.PostResponse
import com.teamsparta.buysell.infra.security.UserPrincipal


interface PostService {
    fun createPost(request: CreatePostRequest, principal: UserPrincipal): PostResponse

    fun updatePost(postId: Int, request: UpdatePostRequest, principal: UserPrincipal): PostResponse

    fun getPosts(): List<PostResponse>

    fun getPostById(postId: Int): PostResponse

    fun deletePost(postId: Int, principal: UserPrincipal)
}