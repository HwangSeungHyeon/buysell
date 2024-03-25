package com.teamsparta.buysell.domain.common.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/health-check")
@RestController
class HealthCheckController {
    @GetMapping
    fun healthCheck(): String{
        return "1"
    }

}