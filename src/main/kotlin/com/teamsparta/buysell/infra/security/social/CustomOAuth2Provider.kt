package com.teamsparta.buysell.infra.security.social

import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.stereotype.Component

@Component
enum class CustomOAuth2Provider {

    KAKAO {
        override fun getBuilder(registrationId: String): ClientRegistration.Builder {
            val builder = getBuilder(registrationId, ClientAuthenticationMethod.CLIENT_SECRET_POST, "{serverUrl}/{action}/oauth2/code/{registrationId}")
            builder.scope("account_email")
            builder.authorizationUri("https://kauth.kakao.com/oauth/authorize")
            builder.tokenUri("https://kauth.kakao.com/oauth/token")
            builder.userInfoUri("https://kapi.kakao.com/v2/user/me")
            builder.userNameAttributeName("id")
            builder.clientName("Kakao")
            return builder
        }
    },
    NAVER {
        override fun getBuilder(registrationId: String): ClientRegistration.Builder {
            val builder = getBuilder(registrationId, ClientAuthenticationMethod.CLIENT_SECRET_POST, "{serverUrl}/{action}/oauth2/code/{registrationId}")
            builder.scope("email")
            builder.authorizationUri("https://nid.naver.com/oauth2.0/authorize")
            builder.tokenUri("https://nid.naver.com/oauth2.0/token")
            builder.userInfoUri("https://openapi.naver.com/v1/nid/me")
            builder.userNameAttributeName("response")
            builder.clientName("Naver")
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