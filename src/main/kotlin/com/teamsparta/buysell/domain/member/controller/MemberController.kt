package com.teamsparta.buysell.domain.member.controller

import com.teamsparta.buysell.domain.member.dto.request.LoginRequest
import com.teamsparta.buysell.domain.member.dto.request.MemberProfileUpdateRequest
import com.teamsparta.buysell.domain.member.dto.request.SignUpRequest
import com.teamsparta.buysell.domain.member.dto.response.MemberResponse
import com.teamsparta.buysell.domain.member.service.MemberService
import com.teamsparta.buysell.domain.post.dto.response.PostResponse
import com.teamsparta.buysell.infra.security.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@Tag(name = "members", description = "멤버 API")
@RestController
@RequestMapping("/members")
class MemberController(
    private val memberService: MemberService,
) {
    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody request: SignUpRequest): ResponseEntity<Void> {
        memberService.signUp(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<String> {
        val token = memberService.login(request)
        return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            .body("로그인 성공.")
    }


    @PutMapping
    @Operation(summary = "프로필 수정", description = "프로필을 수정합니다.")
    fun updateProfile(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody request: MemberProfileUpdateRequest
    ): ResponseEntity<MemberResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.updateMember(userPrincipal, request))
    }

    @GetMapping
    @Operation(summary = "프로필 조회", description = "프로필을 조회합니다.")
    fun getProfile(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): ResponseEntity<MemberResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.getMember(userPrincipal))
    }

    @GetMapping("/posts")
    @Operation(summary = "내가 쓴 게시글 조회", description = "내가 쓴 게시글을 조회합니다.")
    fun getAllPosts(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): ResponseEntity<List<PostResponse>> {
        val posts = memberService.getAllPostByUserPrincipal(userPrincipal)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(posts)
    }

    @GetMapping("/likes")
    @Operation(summary = "내가 찜한 게시글 조회", description = "내가 찜한 게시글을 조회합니다.")
    fun getLikesByMember(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): ResponseEntity<List<PostResponse>> {
        val like = memberService.getAllPostByLike(userPrincipal)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(like)
    }

    @PutMapping("/signout")
    fun pretendDelete(
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ):ResponseEntity<String>{
        memberService.pretendDelete(userPrincipal)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body("탈퇴 신청 완료!")
    }

}