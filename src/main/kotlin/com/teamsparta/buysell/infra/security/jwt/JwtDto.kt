package com.teamsparta.buysell.infra.security.jwt

data class JwtDto(
   val grantType: String,
   val accessToken: String,
   val refreshToken: String,
   val accessTokenExpiresIn: Long
)