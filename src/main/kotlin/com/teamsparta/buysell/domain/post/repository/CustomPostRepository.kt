package com.teamsparta.buysell.domain.post.repository

import com.teamsparta.buysell.domain.post.dto.response.PostListResponse
import com.teamsparta.buysell.domain.post.model.Category
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomPostRepository {
    fun getPostsWithPagination(category: Category?, pageable: Pageable): Page<PostListResponse>

    fun searchByKeyword(keyword: String, pageable: Pageable): Page<PostListResponse>
}