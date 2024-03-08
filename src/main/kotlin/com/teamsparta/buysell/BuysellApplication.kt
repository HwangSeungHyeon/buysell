package com.teamsparta.buysell

import com.teamsparta.buysell.infra.social.CustomOAuth2ClientProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableAspectJAutoProxy // AOP 적용하기 위해서 사용
@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(CustomOAuth2ClientProperties::class)
class BuysellApplication

fun main(args: Array<String>) {
    runApplication<BuysellApplication>(*args)
}
