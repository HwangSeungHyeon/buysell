package com.teamsparta.buysell

import com.teamsparta.buysell.infra.social.CustomOAuth2ClientProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(CustomOAuth2ClientProperties::class)
class BuysellApplication

fun main(args: Array<String>) {
    runApplication<BuysellApplication>(*args)
}
