package com.teamsparta.buysell.domain.member.controller

import com.teamsparta.buysell.domain.member.dto.request.MemberProfileUpdateRequest
import com.teamsparta.buysell.domain.member.dto.response.MemberResponse
import com.teamsparta.buysell.domain.member.dto.response.OtherProfileResponse
import com.teamsparta.buysell.domain.member.dto.response.ProfileResponse
import com.teamsparta.buysell.domain.member.service.ProfileService
import com.teamsparta.buysell.domain.order.dto.response.OrderHistoriesResponse
import com.teamsparta.buysell.domain.post.dto.response.PostListResponse
import com.teamsparta.buysell.infra.security.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/members")
class ProfileController(
    private val profileService: ProfileService
) {
    @GetMapping("/{memberId}/profile/reviews")
    @Operation(summary = "판매자 리뷰 조회하기", description = "판매자가 작성한 게시글에 달린 리뷰를 조회합니다.")
    fun getReviewsByMemberId(
        @PathVariable memberId: Int,
    ): ResponseEntity<ProfileResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(profileService.getReviewsByMemberId(memberId))
    }

    @GetMapping("/{memberId}/profile/orderHistories")
    @Operation(summary = "구매내역 조회하기", description = "구매한 내역을 조회합니다.")
    fun getOrderHistories(
        @PathVariable memberId: Int
    ): ResponseEntity<List<OrderHistoriesResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(profileService.getOrderHistories(memberId))
    }

    @GetMapping("/{memberId}/profile/posts")
    @Operation(summary = "특정 멤버가 작성한 게시글 조회", description = "특정 멤버가 작성한 모든 게시글을 조회합니다.")
    fun getAllPostsMemberId(
        @PathVariable memberId:Int,
    ): ResponseEntity<OtherProfileResponse> {
        val posts = profileService.getAllPostByMemberId(memberId)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(posts)
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('MEMBER')")
    @GetMapping("/my/profile/wishlist")
    @Operation(summary = "내 위시리스트 조회", description = "내 위시리스트를 조회합니다.")
    fun getAllPostByLike(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): ResponseEntity<List<PostListResponse>> {
        val wishList = profileService.getAllPostByWishList(userPrincipal)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(wishList)
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('MEMBER')")
    @PutMapping("/my/profile")
    @Operation(summary = "프로필 수정", description = "프로필을 수정합니다.")
    fun updateMyProfile(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @Valid @RequestBody request: MemberProfileUpdateRequest
    ): ResponseEntity<MemberResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(profileService.updateMyProfile(userPrincipal, request))
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('MEMBER')")
    @GetMapping("/my/profile")
    @Operation(summary = "내 프로필 조회", description = "내 프로필을 조회합니다.")
    fun getMyProfile(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): ResponseEntity<MemberResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(profileService.getMyProfile(userPrincipal))
    }
}