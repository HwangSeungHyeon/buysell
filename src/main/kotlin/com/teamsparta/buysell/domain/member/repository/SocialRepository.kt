package com.teamsparta.buysell.domain.member.repository

import com.teamsparta.buysell.domain.member.model.Social
import org.springframework.data.jpa.repository.JpaRepository

interface SocialRepository: JpaRepository<Social, Int> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): Social?
}