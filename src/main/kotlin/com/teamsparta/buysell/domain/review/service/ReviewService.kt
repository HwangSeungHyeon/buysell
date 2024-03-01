package com.teamsparta.buysell.domain.review.service

import com.teamsparta.buysell.domain.common.dto.MessageResponse
import com.teamsparta.buysell.domain.review.dto.request.CreateReviewRequest
import com.teamsparta.buysell.domain.review.dto.request.UpdateReviewRequest
import com.teamsparta.buysell.infra.security.UserPrincipal

interface ReviewService {

    fun createReview(postId: Int,
                     request: CreateReviewRequest,
                     principal: UserPrincipal): MessageResponse

    fun editReview(reviewId: Int,
                   postId: Int,
                   request: UpdateReviewRequest,
                   principal: UserPrincipal): MessageResponse

    fun deleteReview(postId: Int
                     , reviewId: Int,
                     principal: UserPrincipal): MessageResponse
}
