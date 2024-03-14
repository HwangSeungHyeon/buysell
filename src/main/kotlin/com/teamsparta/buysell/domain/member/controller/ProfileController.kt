package com.teamsparta.buysell.domain.member.controller

import com.teamsparta.buysell.domain.member.dto.request.MemberProfileUpdateRequest
import com.teamsparta.buysell.domain.member.dto.response.MemberResponse
import com.teamsparta.buysell.domain.member.service.ProfileService
import com.teamsparta.buysell.domain.post.dto.response.PostResponse
import com.teamsparta.buysell.infra.security.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/members")
class ProfileController(
    private val profileService: ProfileService
) {
    @GetMapping("/{memberId}/profile/posts")
    @Operation(summary = "특정 멤버가 작성한 게시글 조회", description = "특정 멤버가 작성한 모든 게시글을 조회합니다.")
    fun getAllPostsMemberId(
        @PathVariable memberId:Int,
    ): ResponseEntity<List<PostResponse>> {
        val posts = profileService.getAllPostByMemberId(memberId)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(posts)
    }

    @GetMapping("/my/profile/likes")
    @Operation(summary = "내가 찜한 게시글 조회", description = "내가 찜한 게시글을 조회합니다.")
    fun getAllPostByLike(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): ResponseEntity<List<PostResponse>> {
        val like = profileService.getAllPostByLike(userPrincipal)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(like)
    }

    @PutMapping("/my/profile")
    @Operation(summary = "프로필 수정", description = "프로필을 수정합니다.")
    fun updateMyProfile(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody request: MemberProfileUpdateRequest
    ): ResponseEntity<MemberResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(profileService.updateMyProfile(userPrincipal, request))
    }

    @GetMapping("/my/profile")
    @Operation(summary = "프로필 조회", description = "프로필을 조회합니다.")
    fun getMyProfile(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): ResponseEntity<MemberResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(profileService.getMyProfile(userPrincipal))
    }
}