package com.teamsparta.buysell.domain.review.service

import com.teamsparta.buysell.domain.exception.ModelNotFoundException
import com.teamsparta.buysell.domain.member.repository.MemberRepository
import com.teamsparta.buysell.domain.post.repository.PostRepository
import com.teamsparta.buysell.domain.review.dto.request.CreateReviewRequest
import com.teamsparta.buysell.domain.review.dto.response.ReviewResponse
import com.teamsparta.buysell.domain.review.model.Review
import com.teamsparta.buysell.domain.review.repository.ReviewRepository
import com.teamsparta.buysell.infra.security.UserPrincipal
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ReviewServiceImpl(
    private val reviewRepository: ReviewRepository,
    private val postRepository: PostRepository,
    private val memberRepository: MemberRepository
): ReviewService {
    override fun createReview(postId: Int,
                              request: CreateReviewRequest,
                              principal: UserPrincipal
    ): ReviewResponse {
        val post = postRepository.findByIdOrNull(postId)
            ?: throw ModelNotFoundException("Post", postId)
        val member = memberRepository.findByIdOrNull(principal.id)
            ?: throw ModelNotFoundException("Member", principal.id)

        Review.makeEntity(
            request = request,
            post = post,
            member = member
        ).let { reviewRepository.save(it) }
        return ReviewResponse("리뷰가 작성되었습니다.")
    }
}