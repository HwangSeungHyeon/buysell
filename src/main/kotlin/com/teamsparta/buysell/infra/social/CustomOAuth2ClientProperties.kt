package com.teamsparta.buysell.infra.social

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
            //val jwkSetUri: String = "default"
        }
    }

}
//@Component
//@ConfigurationProperties(prefix = "spring.security.oauth2.client")
//class CustomOAuth2ClientProperties {
//    var registration: Map<String, Registration> = LinkedHashMap()
//}
//
//class Registration {
//    lateinit var clientId: String
//    lateinit var clientSecret: String
//    //lateinit var redirectUri: String
//    var scope: List<String> = ArrayList()
//}