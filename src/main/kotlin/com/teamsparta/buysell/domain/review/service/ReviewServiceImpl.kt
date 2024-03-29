package com.teamsparta.buysell.domain.review.service

import com.teamsparta.buysell.domain.common.dto.MessageResponse
import com.teamsparta.buysell.domain.exception.ModelNotFoundException
import com.teamsparta.buysell.domain.member.repository.MemberRepository
import com.teamsparta.buysell.domain.post.repository.PostRepository
import com.teamsparta.buysell.domain.review.dto.request.CreateReviewRequest
import com.teamsparta.buysell.domain.review.dto.request.UpdateReviewRequest
import com.teamsparta.buysell.domain.review.model.Review
import com.teamsparta.buysell.domain.review.repository.ReviewRepository
import com.teamsparta.buysell.infra.security.UserPrincipal
import jakarta.transaction.Transactional
import org.hibernate.annotations.SQLDelete
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service


@Service
@SQLDelete(sql = "UPDATE review SET is_deleted = true WHERE id = ?") // DELETE 쿼리 대신 실행
class ReviewServiceImpl(
    private val reviewRepository: ReviewRepository,
    private val postRepository: PostRepository,
    private val memberRepository: MemberRepository
) : ReviewService {
    override fun createReview(
        postId: Int,
        request: CreateReviewRequest,
        principal: UserPrincipal
    ): MessageResponse {
        val post = postRepository.findByIdOrNull(postId)
            ?: throw ModelNotFoundException("Post", postId)
        val member = memberRepository.findByIdOrNull(principal.id)
            ?: throw ModelNotFoundException("Member", principal.id)

        val seller = post.member

        if (!post.isSoldOut) {
            throw IllegalStateException("판매되지 않은 물품에 리뷰를 작성할 수 없습니다.")
        }
        // order.memberId == review.memberId 일 경우에 작성가능한 메서드 추가
        // review 중복작성 불가한 예외 추가

        post.myPostCheckPermission(principal)

        val sellerReview = Review.makeEntity(
            request = request,
            post = post,
            member = member
        )
        reviewRepository.save(sellerReview)

        val averageRating = reviewRepository.getAverageRatingByMember(seller.id!!)
        seller.sellerRating = averageRating
        memberRepository.save(seller)

        return MessageResponse("리뷰가 작성되었습니다.")
    }


    @Transactional
    override fun editReview(
        reviewId: Int,
        postId: Int,
        request: UpdateReviewRequest,
        principal: UserPrincipal
    ): MessageResponse {
        val review = reviewRepository.findByIdOrNull(reviewId)
            ?: throw ModelNotFoundException("Review", reviewId)

        review.checkPermission(principal)
        review.editReview(request)

        return MessageResponse("리뷰가 수정되었습니다.")
    }

    @Transactional
    override fun deleteReview(
        postId: Int,
        reviewId: Int,
        principal: UserPrincipal
    ): MessageResponse {
        val review = reviewRepository.findByIdOrNull(reviewId)
            ?: throw ModelNotFoundException("Review", reviewId)

        review.checkPermission(principal)

        reviewRepository.delete(review)

        return MessageResponse("리뷰가 삭제되었습니다.")
    }
}