package com.teamsparta.buysell.infra.security

import com.teamsparta.buysell.domain.member.service.CustomUserDetailService
import com.teamsparta.buysell.infra.security.jwt.CustomAccessDeniedHandler
import com.teamsparta.buysell.infra.security.jwt.JwtAuthenticationFilter
import com.teamsparta.buysell.infra.social.CustomOAuth2ClientProperties
import com.teamsparta.buysell.infra.social.CustomOAuth2Provider
import com.teamsparta.buysell.infra.social.OAuth2LoginSuccessHandler
import com.teamsparta.buysell.infra.social.jwt.SocialJwtAuthenticationFilter
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
    private val socialJwtAuthenticationFilter: SocialJwtAuthenticationFilter,
    private val oAuth2LoginSuccessHandler: OAuth2LoginSuccessHandler
) {
    private val allowedUrls = arrayOf(
        "/", "/swagger-ui/**", "/v3/**","/members/**"
    )

    private val anonymousUrls = arrayOf(
        "/signup", "/login", "**","/members/**"
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
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(socialJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling {
                it.authenticationEntryPoint(authenticationEntryPoint)
                it.accessDeniedHandler(accessDeniedHandler)
            }
        return http.build()
    }


    @Bean
    fun clientRegistrationRepository(oAuth2ClientProperties: OAuth2ClientProperties,
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
        if (customRegistrations != null) {
            for (customRegistration in customRegistrations) {

                when (customRegistration.key) {
                    "kakao" -> registrations.add(
                        CustomOAuth2Provider.KAKAO.getBuilder("kakao")
                            .clientId(customRegistration.value.clientId)
                            .clientSecret(customRegistration.value.clientSecret)
                            .build())
                    "naver" -> registrations.add(
                        CustomOAuth2Provider.NAVER.getBuilder("naver")
                            .clientId(customRegistration.value.clientId)
                            .clientSecret(customRegistration.value.clientSecret)
                            .build())

                }

            }
        }

        return InMemoryClientRegistrationRepository(registrations)
    }

    // 공통 소셜 설정을 호출합니다.
    private fun getRegistration(clientProperties: OAuth2ClientProperties, client: String): ClientRegistration? {
        val registration = clientProperties.registration[client]
        return when(client) {
            "google" -> CommonOAuth2Provider.GOOGLE.getBuilder(client)
                .clientId(registration?.clientId)
                .clientSecret(registration?.clientSecret)
                .scope(registration?.scope)
                .build()


            else -> null
        }
    }


}

//                "naver" -> registrations.add(CustomOAuth2Provider.NAVER.getBuilder("naver")
//                    .clientId(customRegistration.value.clientId)
//                    .clientSecret(customRegistration.value.clientSecret)
//                    .jwkSetUri(customRegistration.value.jwkSetUri)
//                    .build())

//    @Bean
//    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
//        return http.formLogin { it.disable() }
//            .httpBasic { it.disable() }
//            .csrf { it.disable() }
//            .cors { it.disable() }
//            .headers { it.frameOptions { options -> options.sameOrigin() } }
//            .sessionManagement {
//                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            }.oauth2Login { oauthConfig ->
//                oauthConfig.authorizationEndpoint {
//                    it.baseUri("/oauth2/login") // /oauth2/login/kakao
//                }.redirectionEndpoint {
//                    it.baseUri("/oauth2/callback/*") // /oauth2/callback/kakao
//                }.userInfoEndpoint {
//                    it.userService(oAuth2UserService)
//                }.successHandler(oAuth2LoginSuccessHandler)
//            }.build()
//    }