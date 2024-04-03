package com.teamsparta.buysell.domain.review.repository

import com.teamsparta.buysell.domain.review.model.Review
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ReviewRepository: JpaRepository<Review, Int> {

    fun findByPostMemberId(memberId: Int): List<Review>

    fun findByPostIdAndMemberId(postId: Int, memberId: Int): Review?

    @Query ("SELECT AVG(r.rating) FROM Review r WHERE r.post.member.id = :memberId")
    fun getAverageRatingByMember(memberId: Int): Double
}