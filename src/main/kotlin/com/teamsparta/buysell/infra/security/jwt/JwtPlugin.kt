package com.teamsparta.buysell.infra.security.jwt

import com.teamsparta.buysell.domain.member.model.Platform
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.*

@Component
class JwtPlugin(
    @Value("\${auth.jwt.issuer}") private val issuer: String,
    @Value("\${auth.jwt.secret}") private val secret: String,
    @Value("\${auth.jwt.accessTokenExpirationHour}") private val accessTokenExpirationHour : Long,
    @Value("\${auth.jwt.accessTokenExpirationHour}") private val refreshTokenExpirationHour : Long,
) {
    companion object {
        private const val BEARER_TYPE = "bearer"
    }
    fun validateToken(jwt: String): Result<Jws<Claims>>{
       return kotlin.runCatching {
           val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

           Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt)
       }
    }
    fun generateAccessToken(subject: String, email: String, role: String, platform: String): String{
        return generateToken(subject, email, role, platform, java.time.Duration.ofHours(accessTokenExpirationHour))
    }
    fun generateToken(subject: String, email: String, role: String, platform: String, expirationPeriod: java.time.Duration):String{
        val claims:Claims = Jwts.claims()
            .add(mapOf("role" to role,"email" to email, "platform" to platform ))
            .build()
        val now = Instant.now()
        val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
        val expirationPeriod = java.time.Duration.ofHours(2)

        return Jwts.builder()
            .subject(subject)
            .issuer(issuer)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(expirationPeriod)))
            .claims(claims)
            .signWith(key)
            .compact()
    }

    fun generateJwtDto(oAuth2User: OAuth2User, id: String, role:String, platform: Platform) : JwtDto {
        val accessTokenExpiresIn = Date(Date().time + accessTokenExpirationHour)
        val now = Instant.now()
        val expirationPeriod = java.time.Duration.ofHours(2)
        val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
        val email: String? = when (platform.name) {
            "GOOGLE" -> oAuth2User.attributes["email"] as String
            "NAVER" -> {
                val responseAttributes = oAuth2User.attributes["response"] as Map<*, *>
                responseAttributes["email"] as String?
            }
            "KAKAO" -> {
                val kakaoAttributes = oAuth2User.attributes["kakao_account"] as Map<*, *>
                kakaoAttributes["email"] as String?
            }
            else -> null
        }

        val claims:Claims = Jwts.claims()
            .add(mapOf("role" to role, "email" to email,"platform" to platform))
            .build()
        val accessToken = Jwts.builder()
            .subject(id) // payload "sub": "id"
            .issuer(issuer)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(expirationPeriod)))
            .claims(claims)
            .signWith(key) // header "alg": "HS512"
            .compact()


        val refreshToken = Jwts.builder()
            .expiration(Date(Date().time+refreshTokenExpirationHour))
            .signWith(key)
            .compact()

        return JwtDto(
            grantType = BEARER_TYPE,
            accessToken = accessToken,
            refreshToken = refreshToken,
            accessTokenExpiresIn = accessTokenExpiresIn.time
        )
    }
}