package com.teamsparta.buysell

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class BuysellApplication

fun main(args: Array<String>) {
    runApplication<BuysellApplication>(*args)
}
