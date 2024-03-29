package com.teamsparta.buysell.domain.member.controller

import com.teamsparta.buysell.domain.common.dto.MessageResponse
import com.teamsparta.buysell.domain.member.dto.request.LoginRequest
import com.teamsparta.buysell.domain.member.dto.request.SignUpRequest
import com.teamsparta.buysell.domain.member.service.AuthLinkService
import com.teamsparta.buysell.domain.member.service.MemberService
import com.teamsparta.buysell.domain.member.service.SocialService
import com.teamsparta.buysell.infra.security.UserPrincipal
import com.teamsparta.buysell.infra.security.jwt.JwtDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
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
    fun sendEmail(@RequestParam ("email") email: String): ResponseEntity<MessageResponse> {
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
            .header(HttpHeaders.AUTHORIZATION, token)
            .body("로그인 성공했습니다.")
    }



    @PreAuthorize("hasRole('ADMIN') OR hasRole('MEMBER')")
    @PutMapping("/signout")
    fun signOut(
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ):ResponseEntity<String>{
        memberService.signOut(userPrincipal)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body("회원탈퇴가 완료되었습니다.")
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

    @GetMapping("/kakao/callback")
    @Operation(summary = "카카오 엑세스 토큰 발급", description = "카카오 엑세스 토큰을 발급 합니다.")
    fun kakaoLogin(httpServletResponse: HttpServletResponse, @AuthenticationPrincipal oAuth2User: OAuth2User?) {
        if (oAuth2User == null) {
            throw BadCredentialsException("인증된 사용자가 없습니다")
        }

        // 카카오 로그인을 통해 받은 토큰 가져오기
        val token = socialService.kakaoLogin(oAuth2User).accessToken

        // 쿠키 생성 및 설정
        val cookie = Cookie("token", token)
        cookie.maxAge = 3600 // 쿠키 만료 시간 (초 단위)
        cookie.path = "/" // 모든 경로에서 쿠키 사용
        httpServletResponse.addCookie(cookie)

        // 메인 페이지 URL을 응답으로 전달
        val mainUrl = "http://localhost:3000"
        httpServletResponse.sendRedirect(mainUrl)
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