package com.teamsparta.buysell.domain.review.repository

import com.teamsparta.buysell.domain.review.model.Review
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository: JpaRepository<Review, Int> {

    fun findByPostIdAndId(postId: Int, reviewId: Int): Review
}