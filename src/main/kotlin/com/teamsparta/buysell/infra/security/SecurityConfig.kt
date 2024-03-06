package com.teamsparta.buysell.infra.security

import com.teamsparta.buysell.infra.security.jwt.CustomAccessDeniedHandler
import com.teamsparta.buysell.infra.security.jwt.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val authenticationEntryPoint: AuthenticationEntryPoint,
    private val accessDeniedHandler: CustomAccessDeniedHandler,
) {
    private val allowedUrls = arrayOf(
        "/",
        "/swagger-ui/**",
        "/v3/**",
        "/posts/**",
        "/members/**"
    )

    private val anonymousUrls = arrayOf(
        "/signup",
        "/login",
        "**",

    )

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .csrf { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.NEVER)
            }
            .authorizeHttpRequests {
                it.requestMatchers(*allowedUrls).permitAll()
                    .requestMatchers(*anonymousUrls).anonymous()//익명 사용자만 접근 가능
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling {
                it.authenticationEntryPoint(authenticationEntryPoint)
                it.accessDeniedHandler(accessDeniedHandler)
            }
        return http.build()
    }
}