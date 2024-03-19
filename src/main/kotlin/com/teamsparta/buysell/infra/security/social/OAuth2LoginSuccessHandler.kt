package com.teamsparta.buysell.infra.security.social


import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2LoginSuccessHandler : SimpleUrlAuthenticationSuccessHandler() {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val clientRegistrationId = (authentication as OAuth2AuthenticationToken).authorizedClientRegistrationId

        when (clientRegistrationId) {
            "google" -> this.defaultTargetUrl = "/members/google/callback"
            "kakao" -> this.defaultTargetUrl = "/members/kakao/callback"
            "naver" -> this.defaultTargetUrl = "/members/naver/callback"
        }

        super.onAuthenticationSuccess(request, response, authentication)
    }
}