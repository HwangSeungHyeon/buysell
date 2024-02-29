package com.teamsparta.buysell

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableAspectJAutoProxy // AOP 적용하기 위해서 사용
@EnableJpaAuditing //JPA에서 자동으로 값을 넣어줌
@SpringBootApplication
class BuysellApplication

fun main(args: Array<String>) {
    runApplication<BuysellApplication>(*args)
}
