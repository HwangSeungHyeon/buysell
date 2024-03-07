package com.teamsparta.buysell.domain.member.controller

import com.teamsparta.buysell.domain.member.dto.request.LoginRequest
import com.teamsparta.buysell.domain.member.dto.request.SignUpRequest
import com.teamsparta.buysell.domain.member.service.SocialService
import com.teamsparta.buysell.domain.member.service.MemberService
import com.teamsparta.buysell.infra.social.jwt.JwtDto
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/members")
class MemberController(
    private val memberService: MemberService,
    private val socialService: SocialService,
) {
    //로컬 회원가입
    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody request: SignUpRequest): ResponseEntity<Void> {
        memberService.signUp(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
    //로컬 로그인
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<String>{
        val token = memberService.login(request)
        return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            .body("로그인 성공.")
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