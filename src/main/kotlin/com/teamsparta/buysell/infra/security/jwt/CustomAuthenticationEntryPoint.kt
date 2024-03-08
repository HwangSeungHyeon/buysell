package com.teamsparta.buysell.infra.security.jwt

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationEntryPoint: AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ){
        // 예외를 처리하고 적절한 응답을 만드는 로직
        val exception = request.getAttribute("exception") as Exception
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.message)
    }
}