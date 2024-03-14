package com.teamsparta.buysell.domain.member.controller

import com.teamsparta.buysell.domain.member.service.ProfileService
import com.teamsparta.buysell.domain.post.dto.response.PostResponse
import com.teamsparta.buysell.infra.security.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/members/{memberId}/profile")
class ProfileController(
    private val profileService: ProfileService
) {
    @GetMapping("/posts")
    @Operation(summary = "작성한 게시글 조회", description = "작성한 모든 게시글을 조회합니다.")
    fun getAllPostsMemberId(
        @PathVariable memberId:Int,
    ): ResponseEntity<List<PostResponse>> {
        val posts = profileService.getAllPostByMemberId(memberId)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(posts)
    }

    @GetMapping("/likes")
    @Operation(summary = "내가 찜한 게시글 조회", description = "내가 찜한 게시글을 조회합니다.")
    fun getLikesByMember(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): ResponseEntity<List<PostResponse>> {
        val like = profileService.getAllPostByLike(userPrincipal)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(like)
    }
}