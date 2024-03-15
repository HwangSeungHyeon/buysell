package com.teamsparta.buysell.domain.member.service

import com.teamsparta.buysell.domain.exception.ModelNotFoundException
import com.teamsparta.buysell.domain.member.dto.request.MemberProfileUpdateRequest
import com.teamsparta.buysell.domain.member.dto.response.MemberResponse
import com.teamsparta.buysell.domain.member.repository.MemberRepository
import com.teamsparta.buysell.domain.post.dto.response.PostResponse
import com.teamsparta.buysell.domain.post.model.toResponse
import com.teamsparta.buysell.domain.post.repository.LikeRepository
import com.teamsparta.buysell.domain.post.repository.PostRepository
import com.teamsparta.buysell.infra.security.UserPrincipal
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProfileServiceImpl(
    private val memberRepository: MemberRepository,
    private val likeRepository: LikeRepository,
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

    //내가 찜 한 글 전체 조회
    override fun getAllPostByLike(userPrincipal: UserPrincipal): List<PostListResponse>? {
        val member = memberRepository.findByIdOrNull(userPrincipal.id)
            ?: throw ModelNotFoundException("member", userPrincipal.id)
        val like = wishListRepository.findByMember(member)
        val post = like.map { it.post }
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