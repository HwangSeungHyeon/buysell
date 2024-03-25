package com.teamsparta.buysell.infra.security.social

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client")
class CustomOAuth2ClientProperties {

    lateinit var registration: Map<String, Registration>

    companion object {
        class Registration {
            lateinit var clientId: String
            var clientSecret: String = "default"
            var redirectUri: String = "default"
        }
    }
}
