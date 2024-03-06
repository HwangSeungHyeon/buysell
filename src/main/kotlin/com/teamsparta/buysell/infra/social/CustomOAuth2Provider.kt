package com.teamsparta.buysell.infra.social

import org.springframework.security.config.oauth2.client.CommonOAuth2Provider
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.stereotype.Component

@Component
enum class CustomOAuth2Provider {

    KAKAO {
        override fun getBuilder(registrationId: String): ClientRegistration.Builder {
            val builder = getBuilder(registrationId, ClientAuthenticationMethod.CLIENT_SECRET_POST, "{baseUrl}/{action}/oauth2/code/{registrationId}")
            builder.scope("account_email")
            builder.authorizationUri("https://kauth.kakao.com/oauth/authorize")
            builder.tokenUri("https://kauth.kakao.com/oauth/token")
            builder.userInfoUri("https://kapi.kakao.com/v2/user/me")
            builder.userNameAttributeName("id")
            builder.clientName("Kakao")
            return builder
        }
    };

    protected fun getBuilder(
        registrationId: String,
        method: ClientAuthenticationMethod,
        redirectUri: String): ClientRegistration.Builder {

        return ClientRegistration.withRegistrationId(registrationId)
            .clientAuthenticationMethod(method)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri(redirectUri)
    }

    abstract fun getBuilder(registrationId: String): ClientRegistration.Builder
}