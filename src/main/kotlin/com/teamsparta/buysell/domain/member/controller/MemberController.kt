package com.teamsparta.buysell.domain.member.controller

import com.teamsparta.buysell.domain.common.dto.MessageResponse
import com.teamsparta.buysell.domain.member.dto.request.LoginRequest
import com.teamsparta.buysell.domain.member.dto.request.MemberProfileUpdateRequest
import com.teamsparta.buysell.domain.member.dto.request.SignUpRequest
import com.teamsparta.buysell.domain.member.dto.response.MemberResponse
import com.teamsparta.buysell.domain.member.service.AuthLinkService
import com.teamsparta.buysell.domain.member.service.MemberService
import com.teamsparta.buysell.domain.member.service.SocialService
import com.teamsparta.buysell.domain.post.dto.response.PostResponse
import com.teamsparta.buysell.infra.security.UserPrincipal
import com.teamsparta.buysell.infra.social.jwt.JwtDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
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
    private val authLinkService: AuthLinkService,
) {
    //로컬 회원가입
    @PreAuthorize("isAnonymous()")
    @Operation(summary = "회원 가입", description = "회원 가입을 합니다.")
    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody request: SignUpRequest): ResponseEntity<Void> {
        memberService.signUp(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
    @PreAuthorize("isAnonymous()")
    @Operation(summary = "인증 링크 확인", description = "이메일로 인증 처리를 합니다.")
    @GetMapping("/verify")
    fun verifyMember(@RequestParam ("email") email: String, @RequestParam ("token") token: String): ResponseEntity<MessageResponse> {
        val verifyMember = authLinkService.verifyMember(email, token)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(verifyMember)
    }
    @PreAuthorize("isAnonymous()")
    @Operation(summary = "인증 링크 메시지 전송", description = "이메일로 인증 링크를 전송합니다.")
    @PostMapping("/sendemail")
    fun sendEmail(email: String): ResponseEntity<MessageResponse> {
        val sendEmail = authLinkService.sendAuthEmail(email)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(sendEmail)
    }

    //로컬 로그인
    @PreAuthorize("isAnonymous()")
    @Operation(summary = "로그인", description = "로그인을 합니다.")
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<String>{
        val token = memberService.login(request)
        return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, "Bearer ${token}")
            .body("로그인 성공했습니다.")
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
    @Operation(summary = "구글 로그인 페이지", description = "구글 로그인 페이지를 불러옵니다.")
    fun getGoogleLoginPage(): ResponseEntity<Any?> {
        return ResponseEntity<Any?>(socialService.getGoogleLoginPage(), HttpStatus.OK)
    }
    //구글 로그인 엑세스토큰 발급
    @GetMapping("/google/callback")
    @Operation(summary = "구글 엑세스 토큰 발급", description = "구글 엑세스 토큰을 발급 합니다.")
    fun googleLogin(@AuthenticationPrincipal oAuth2User: OAuth2User?): ResponseEntity<JwtDto> {
        if (oAuth2User == null) {
            throw BadCredentialsException("인증된 사용자가 없습니다")
        }
        return ResponseEntity.ok(socialService.googleLogin(oAuth2User))
    }
    @GetMapping("/kakao/page")
    @Operation(summary = "카카오 로그인 페이지", description = "카카오 로그인 페이지를 불러옵니다.")
    fun getKakaoLoginPage(): ResponseEntity<Any?> {
        return ResponseEntity<Any?>(socialService.getKakaoLoginPage(), HttpStatus.OK)
    }

    //카카오 로그인 엑세스토큰 발급
    @GetMapping("/kakao/callback")
    @Operation(summary = "카카오 엑세스 토큰 발급", description = "카카오 엑세스 토큰을 발급 합니다.")
    fun kakaoLogin(@AuthenticationPrincipal oAuth2User: OAuth2User?): ResponseEntity<JwtDto> {
        if (oAuth2User == null) {
            throw BadCredentialsException("인증된 사용자가 없습니다")
        }
        return ResponseEntity.ok(socialService.kakaoLogin(oAuth2User))
    }
    @GetMapping("/naver/page")
    @Operation(summary = "네이버 로그인 페이지", description = "네이버 로그인 페이지를 불러옵니다.")
    fun getNaverLoginPage(): ResponseEntity<Any?> {
        return ResponseEntity<Any?>(socialService.getNaverLoginPage(), HttpStatus.OK)
    }
    //네이버 로그인 엑세스토큰 발급
    @GetMapping("/naver/callback")
    @Operation(summary = "네이버 엑세스 토큰 발급", description = "네이버 엑세스 토큰을 발급 합니다.")
    fun naverLogin(@AuthenticationPrincipal oAuth2User: OAuth2User?): ResponseEntity<JwtDto> {
        if (oAuth2User == null) {
            throw BadCredentialsException("인증된 사용자가 없습니다")
        }
        return ResponseEntity.ok(socialService.naverLogin(oAuth2User))
    }
}