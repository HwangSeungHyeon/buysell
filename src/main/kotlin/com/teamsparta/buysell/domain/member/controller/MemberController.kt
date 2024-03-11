package com.teamsparta.buysell.domain.member.controller

import com.teamsparta.buysell.domain.member.dto.request.LoginRequest
import com.teamsparta.buysell.domain.member.dto.request.MemberProfileUpdateRequest
import com.teamsparta.buysell.domain.member.dto.request.SignUpRequest
import com.teamsparta.buysell.domain.member.dto.request.VerifyRequest
import com.teamsparta.buysell.domain.member.dto.response.MemberResponse
import com.teamsparta.buysell.domain.member.model.VerifyResult
import com.teamsparta.buysell.domain.member.service.MemberService
import com.teamsparta.buysell.domain.post.dto.response.PostResponse
import com.teamsparta.buysell.infra.security.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import com.teamsparta.buysell.domain.member.service.SocialService
import com.teamsparta.buysell.infra.social.jwt.JwtDto
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.*


@Tag(name = "members", description = "멤버 API")
@RestController
@RequestMapping("/members")
class MemberController(
    private val memberService: MemberService,
    private val socialService: SocialService,
) {
    //로컬 회원가입
    @PreAuthorize("isAnonymous()")
    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody request: SignUpRequest): ResponseEntity<Void> {
        memberService.signUp(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
    @PreAuthorize("isAnonymous()")
    @PostMapping("/verify")
    fun verifyMember(@RequestBody request: VerifyRequest): ResponseEntity<String> {
        val result = memberService.verifyMember(request.memberId, request.inputVerificationCode)
        return ResponseEntity.ok(result)
    }
    @PreAuthorize("isAnonymous()")
    @PostMapping("/resend-code")
    fun regenerateAuthCode(memberId: String): ResponseEntity<String> {
        val result = memberService.regenerateAuthCode(memberId)
        return ResponseEntity.ok(result)
    }

    //로컬 로그인
    @PreAuthorize("isAnonymous()")
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<String>{
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

    //소셜로그인
    //구글 로그인 페이지 불러오기
    @GetMapping("/google/page")
    fun getGoogleLoginPage(): ResponseEntity<Any?> {
        return ResponseEntity<Any?>(socialService.getGoogleLoginPage(), HttpStatus.OK)
    }
    //구글 로그인 엑세스토큰 발급
    @GetMapping("/google/callback")
    fun googleLogin(@AuthenticationPrincipal oAuth2User: OAuth2User?): ResponseEntity<JwtDto> {
        if (oAuth2User == null) {
            throw BadCredentialsException("인증된 사용자가 없습니다")
        }
        return ResponseEntity.ok(socialService.googleLogin(oAuth2User))
    }
    @GetMapping("/kakao/page")
    fun getKakaoLoginPage(): ResponseEntity<Any?> {
        return ResponseEntity<Any?>(socialService.getKakaoLoginPage(), HttpStatus.OK)
    }

    //카카오 로그인 엑세스토큰 발급
    @GetMapping("/kakao/callback")
    fun kakaoLogin(@AuthenticationPrincipal oAuth2User: OAuth2User?): ResponseEntity<JwtDto> {
        if (oAuth2User == null) {
            throw BadCredentialsException("인증된 사용자가 없습니다")
        }
        return ResponseEntity.ok(socialService.kakaoLogin(oAuth2User))
    }
    @GetMapping("/naver/page")
    fun getNaverLoginPage(): ResponseEntity<Any?> {
        return ResponseEntity<Any?>(socialService.getNaverLoginPage(), HttpStatus.OK)
    }
    //네이버 로그인 엑세스토큰 발급
    @GetMapping("/naver/callback")
    fun naverLogin(@AuthenticationPrincipal oAuth2User: OAuth2User?): ResponseEntity<JwtDto> {
        if (oAuth2User == null) {
            throw BadCredentialsException("인증된 사용자가 없습니다")
        }
        return ResponseEntity.ok(socialService.naverLogin(oAuth2User))
    }
}