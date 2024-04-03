package com.teamsparta.buysell.domain.member.service

import com.teamsparta.buysell.domain.exception.ModelNotFoundException
import com.teamsparta.buysell.domain.member.dto.request.MemberProfileUpdateRequest
import com.teamsparta.buysell.domain.member.dto.response.MemberResponse
import com.teamsparta.buysell.domain.member.dto.response.OtherProfileResponse
import com.teamsparta.buysell.domain.member.dto.response.ProfileResponse
import com.teamsparta.buysell.domain.member.repository.MemberRepository
import com.teamsparta.buysell.domain.order.dto.response.OrderHistoriesResponse
import com.teamsparta.buysell.domain.order.repository.OrderRepository
import com.teamsparta.buysell.domain.post.dto.response.PostListResponse
import com.teamsparta.buysell.domain.post.repository.PostRepository
import com.teamsparta.buysell.domain.post.repository.WishListRepository
import com.teamsparta.buysell.domain.review.model.toResponse
import com.teamsparta.buysell.domain.review.repository.ReviewRepository
import com.teamsparta.buysell.infra.security.UserPrincipal
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProfileServiceImpl(
    private val memberRepository: MemberRepository,
    private val wishListRepository: WishListRepository,
    private val reviewRepository: ReviewRepository,
    private val postRepository: PostRepository,
    private val orderRepository: OrderRepository,
) : ProfileService {
    override fun getReviewsByMemberId(memberId: Int): ProfileResponse {
        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw ModelNotFoundException("Member", memberId)
        val review = reviewRepository.findByPostMemberId(memberId)

        return ProfileResponse(member.nickname, review.map { it.toResponse() })
    }

    override fun getOrderHistories(memberId: Int): List<OrderHistoriesResponse> {
        val orders = orderRepository.findByMemberId(memberId)
        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw ModelNotFoundException("Member", memberId)

        val histories = mutableListOf<OrderHistoriesResponse>()

        orders.forEach { order ->
            val posts = postRepository.findByOrderId(order.id)
            posts.forEach { post ->
                histories.add(
                    OrderHistoriesResponse(
                        imageUrl = post.imageUrl,
                        postTitle = post.title,
                        price = post.price,
                        postId = post.id
                    )
                )
            }
        }
        return histories
    }


    override fun getAllPostByMemberId(memberId: Int): OtherProfileResponse {
        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw ModelNotFoundException("member", memberId)

        return OtherProfileResponse(
            nickname = member.nickname,
            rating = member.sellerRating,
            post = member.post.map { it.toResponse() }
        )
    }

    //내 위시리스트 전체 조회
    override fun getAllPostByWishList(userPrincipal: UserPrincipal): List<PostListResponse>? {
        val member = memberRepository.findByIdOrNull(userPrincipal.id)
            ?: throw ModelNotFoundException("member", userPrincipal.id)
        val wishList = wishListRepository.findByMember(member)
        val post = wishList.map { it.post }
        return post.map { it.toListResponse() }
    }

    // 현재 로그인 한 멤버 아이디 기준 정보 조회
    @Transactional
    override fun getMyProfile(userPrincipal: UserPrincipal): MemberResponse? {
        val member = memberRepository.findByIdOrNull(userPrincipal.id)
            ?: throw ModelNotFoundException("Member", userPrincipal.id)
        return member.toResponse()
    }

    // 로그인 한 멤버 아이디 기준 정보 수정
    @Transactional
    override fun updateMyProfile(userPrincipal: UserPrincipal, request: MemberProfileUpdateRequest): MemberResponse {
        if (memberRepository.existsByNickname(request.nickname))
            throw DataIntegrityViolationException("이미 사용 중인 닉네임입니다.")

        val member = memberRepository.findByIdOrNull(userPrincipal.id)
            ?: throw ModelNotFoundException("Member", userPrincipal.id)
        member.nickname = request.nickname
        return member.toResponse()
    }

}