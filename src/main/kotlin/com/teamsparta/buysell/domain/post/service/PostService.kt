package com.teamsparta.buysell.domain.post.service

import com.teamsparta.buysell.domain.post.dto.request.CreatePostRequest
import com.teamsparta.buysell.domain.post.dto.request.UpdatePostRequest
import com.teamsparta.buysell.domain.post.dto.response.PostResponse


interface PostService {
    fun createPost(request: CreatePostRequest): PostResponse

    fun updatePost(postId: Int, request: UpdatePostRequest): PostResponse

    fun getPosts(): List<PostResponse>

    fun getPostById(postId: Int): PostResponse

    fun deletePost(postId: Int)
}