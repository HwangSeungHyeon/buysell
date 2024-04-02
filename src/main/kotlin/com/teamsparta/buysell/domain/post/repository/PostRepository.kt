package com.teamsparta.buysell.domain.post.repository

import com.teamsparta.buysell.domain.post.model.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PostRepository: JpaRepository<Post, Int>, CustomPostRepository {

    @Query("SELECT p FROM Post p WHERE p.order.id = :orderId")
    fun findByOrderId(orderId: Int?): List<Post>
}