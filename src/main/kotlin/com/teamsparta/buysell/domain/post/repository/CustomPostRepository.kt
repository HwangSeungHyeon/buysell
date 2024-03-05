package com.teamsparta.buysell.domain.post.repository

import com.teamsparta.buysell.domain.post.dto.response.PostListResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomPostRepository {
    fun searchByKeyword(pageable: Pageable): Page<PostListResponse>
}