package com.teamsparta.buysell.domain.member.service

import com.teamsparta.buysell.domain.exception.ModelNotFoundException
import com.teamsparta.buysell.domain.member.dto.request.MemberProfileUpdateRequest
import com.teamsparta.buysell.domain.member.dto.response.MemberResponse
import com.teamsparta.buysell.domain.member.dto.response.OtherProfileResponse
import com.teamsparta.buysell.domain.member.repository.MemberRepository
import com.teamsparta.buysell.domain.post.dto.response.PostListResponse
import com.teamsparta.buysell.domain.post.repository.WishListRepository
import com.teamsparta.buysell.domain.review.dto.response.ReviewResponse
import com.teamsparta.buysell.domain.review.model.toResponse
import com.teamsparta.buysell.domain.review.repository.ReviewRepository
import com.teamsparta.buysell.infra.security.UserPrincipal
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProfileServiceImpl(
    private val memberRepository: MemberRepository,
    private val wishListRepository: WishListRepository,
    private val reviewRepository: ReviewRepository
) : ProfileService {
    override fun getReviewsByMemberId(memberId: Int): List<ReviewResponse> {
        val review = reviewRepository.findByPostMemberId(memberId)

        return review.map { it.toResponse() }
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
        val member = memberRepository.findByIdOrNull(userPrincipal.id)
            ?: throw ModelNotFoundException("Member", userPrincipal.id)
        member.nickname = request.nickname
        member.birthday = request.birthday
        return memberRepository.save(member).toResponse()
    }

}