package com.teamsparta.buysell.infra.social

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client")
class CustomOAuth2ClientProperties {
    var registration: Map<String, Registration> = LinkedHashMap()
}

class Registration {
    lateinit var clientId: String
    lateinit var clientSecret: String
    //lateinit var redirectUri: String
    var scope: List<String> = ArrayList()
}