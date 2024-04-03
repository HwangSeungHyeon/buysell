
//package com.teamsparta.buysell.infra.security
//
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.web.cors.CorsConfiguration
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource
//import org.springframework.web.filter.CorsFilter
//
//@Configuration
//class CorsConfig {
//    @Bean
//    fun corsFilter(): CorsFilter {
//        val source = UrlBasedCorsConfigurationSource()
//        val config = CorsConfiguration().apply {
//            allowCredentials = true
//            addAllowedOriginPattern("*")
//            addAllowedHeader("*")
//            addAllowedMethod("*") // 요청 허용 메서드
//            addExposedHeader("authorization") // 클라이언트에 노출할 헤더 추가
//        }
//        source.registerCorsConfiguration("/**", config)
//        return CorsFilter(source)
//    }
//}
