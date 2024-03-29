package com.teamsparta.buysell.infra.security

import com.teamsparta.buysell.domain.member.service.CustomUserDetailService
import com.teamsparta.buysell.infra.security.jwt.CustomAccessDeniedHandler
import com.teamsparta.buysell.infra.security.jwt.JwtAuthenticationFilter
import com.teamsparta.buysell.infra.security.social.CustomOAuth2ClientProperties
import com.teamsparta.buysell.infra.security.social.CustomOAuth2Provider
import com.teamsparta.buysell.infra.security.social.OAuth2LoginSuccessHandler
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
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
    private val customUserDetailService: CustomUserDetailService,
    private val oAuth2LoginSuccessHandler: OAuth2LoginSuccessHandler
) {
    private val allowedUrls = arrayOf(
        "/", "/swagger-ui/**", "/v3/**",
        "/members/**", "/posts/**", "/accounts/**", "/coupons/**", "/orders/**", "/images/**",
        "/health-check"
    )

    private val anonymousUrls = arrayOf(
        "**"
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
            .oauth2Login {
                it.userInfoEndpoint { u -> u.userService(customUserDetailService) }
                it.successHandler(oAuth2LoginSuccessHandler)
                it.failureUrl("/loginFailure") // 로그인 실패 시 리다이렉트할 URL

            }
            .authorizeHttpRequests {
                it.requestMatchers(*allowedUrls).permitAll()
                    .requestMatchers(*anonymousUrls).anonymous() //익명 사용자만 접근 가능
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling {
                it.authenticationEntryPoint(authenticationEntryPoint)
                it.accessDeniedHandler(accessDeniedHandler)
            }
        return http.build()
    }


    @Bean
    fun clientRegistrationRepository(
        oAuth2ClientProperties: OAuth2ClientProperties,
        customOAuth2ClientProperties: CustomOAuth2ClientProperties
    ): InMemoryClientRegistrationRepository {

        // 소셜 설정 등록
        val registrations = oAuth2ClientProperties.registration.keys
            .map { getRegistration(oAuth2ClientProperties, it) }
            .filter { it != null }
            .toMutableList()

        // 추가 설정 프로퍼티
        val customRegistrations = customOAuth2ClientProperties.registration

        // 추가 소셜 설정을 기본 소셜 설정에 추가
        for (customRegistration in customRegistrations) {
            when (customRegistration.key) {
                "kakao" -> registrations.add(
                    CustomOAuth2Provider.KAKAO.getBuilder("kakao")
                        .clientId(customRegistration.value.clientId)
                        .clientSecret(customRegistration.value.clientSecret)
                        .redirectUri(customRegistration.value.redirectUri)
                        .build()
                )

                "naver" -> registrations.add(
                    CustomOAuth2Provider.NAVER.getBuilder("naver")
                        .clientId(customRegistration.value.clientId)
                        .clientSecret(customRegistration.value.clientSecret)
                        .redirectUri(customRegistration.value.redirectUri)
                        .build()
                )
            }
        }
        return InMemoryClientRegistrationRepository(registrations)
    }

    // 공통 소셜 설정을 호출합니다.
    private fun getRegistration(clientProperties: OAuth2ClientProperties, client: String): ClientRegistration? {
        val registration = clientProperties.registration[client]
        return when (client) {
            "google" -> CommonOAuth2Provider.GOOGLE.getBuilder(client)
                .clientId(registration?.clientId)
                .clientSecret(registration?.clientSecret)
                .redirectUri(registration?.redirectUri)
                .scope(registration?.scope)
                .build()

            else -> null
        }
    }
}