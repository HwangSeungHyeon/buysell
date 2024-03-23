package com.teamsparta.buysell

import com.teamsparta.buysell.infra.security.social.CustomOAuth2ClientProperties
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@OpenAPIDefinition(servers = [Server(url = ("\${BASE_URL}"), description = "Default Server URL")]) //https
@EnableAspectJAutoProxy // AOP 적용하기 위해서 사용
@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(CustomOAuth2ClientProperties::class)
class BuysellApplication

fun main(args: Array<String>) {
    runApplication<BuysellApplication>(*args)
}
