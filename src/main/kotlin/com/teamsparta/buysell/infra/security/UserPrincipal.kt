package com.teamsparta.buysell.infra.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class UserPrincipal(
    val id: Int,
    val email: String,
    val authorities : Collection<GrantedAuthority>,
    val platform: String
) {
    constructor(id: Int, email: String, role: Set<String>, platform: String): this(
        id,
        email,
        role.map{SimpleGrantedAuthority("ROLE_$it")},
        platform
    )
}