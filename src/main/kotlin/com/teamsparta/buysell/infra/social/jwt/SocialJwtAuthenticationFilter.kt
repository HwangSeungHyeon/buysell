package com.teamsparta.buysell.infra.social.jwt

import com.teamsparta.buysell.infra.security.UserPrincipal
import com.teamsparta.buysell.infra.security.jwt.JwtAuthenticationToken
import jakarta.security.auth.message.AuthException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class SocialJwtAuthenticationFilter (
    private val jwtProvider : JwtProvider
): OncePerRequestFilter() {

    companion object {
        private val BEARER_PATTERN = Regex("^Bearer (.+?)$")
    }
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val jwt = request.getBearerToken()

        if (jwt != null) {
            jwtProvider.validateToken(jwt)
                .onSuccess {
                    val memberId = it.payload.subject.toInt()
                    val role = it.payload.get("role", String::class.java)
                    val email = it.payload.get("email", String::class.java)
                    val platform = it.payload.get("platform", String::class.java)
                    val principal = UserPrincipal(
                        id = memberId,
                        email = email,
                        role = setOf(role),
                        platform = platform
                    )
                    val authentication = JwtAuthenticationToken (
                        principal = principal,
                        details = WebAuthenticationDetailsSource().buildDetails(request)
                    )
                    SecurityContextHolder.getContext().authentication = authentication
                }
                .onFailure {
                    // 토큰이 유효하지 않는 경우, 에러 메시지 출력
                        e -> request.setAttribute("exception", e)
                }
        }else{ //추가된 부분
            request.setAttribute("exception", AuthException("로그인이 필요한 서비스입니다."))
        }

        filterChain.doFilter(request, response)

    }

    private fun HttpServletRequest.getBearerToken(): String? {
        val headerValue = this.getHeader(HttpHeaders.AUTHORIZATION) ?: return null // Bearer {JWT}
        return BEARER_PATTERN.find(headerValue)?.groupValues?.get(1)
    }

}