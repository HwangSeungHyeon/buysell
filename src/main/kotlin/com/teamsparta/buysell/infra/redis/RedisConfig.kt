//package com.teamsparta.buysell.infra.redis
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
//import com.fasterxml.jackson.module.kotlin.registerKotlinModule
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.data.redis.cache.RedisCacheConfiguration
//import org.springframework.data.redis.cache.RedisCacheManager
//import org.springframework.data.redis.connection.RedisConnectionFactory
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
//import org.springframework.data.redis.core.RedisTemplate
//import org.springframework.data.redis.serializer.RedisSerializationContext
//import org.springframework.data.redis.serializer.StringRedisSerializer
//import java.time.Duration
//import java.util.concurrent.TimeUnit
//
//@Configuration
//class RedisConfig(
//    @Value("\${spring.data.redis.host}") val host: String,
//    @Value("\${spring.data.redis.port}") val port: Int,
//) {
//    @Bean
//    fun redisConnectionFactory(): RedisConnectionFactory{
//        return LettuceConnectionFactory()
//    }
//
//    @Bean
//    fun redisTemplate(
//        redisConnectionFactory: RedisConnectionFactory
//    ): RedisTemplate<String, String>{
//        val template = RedisTemplate<String, String>()
//        template.connectionFactory = redisConnectionFactory
//
//        //String 자료구조를 위한 Serializer
//        template.keySerializer = StringRedisSerializer()
//        template.valueSerializer = StringRedisSerializer()
//
//        //Hash 자료구조를 위한 Serializer
//        template.hashKeySerializer = StringRedisSerializer()
//        template.hashValueSerializer = StringRedisSerializer()
//
//        return template
//    }
//
////    @Bean
////    fun cacheManager(
////        redisConnectionFactory: RedisConnectionFactory
////    ): RedisCacheManager{
////        val defaults: RedisCacheConfiguration = RedisCacheConfiguration
////            .defaultCacheConfig()
////            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
////            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
////            .entryTtl(Duration.ofMinutes(30)) //캐시 유효기간을 30분으로 설정
////
////        return RedisCacheManager
////            .RedisCacheManagerBuilder
////            .fromConnectionFactory(redisConnectionFactory)
////            .cacheDefaults(defaults).build()
////    }
//}