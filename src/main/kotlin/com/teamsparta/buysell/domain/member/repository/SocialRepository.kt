package com.teamsparta.buysell.domain.member.repository

import com.teamsparta.buysell.domain.member.model.Platform
import com.teamsparta.buysell.domain.member.model.Social
import org.springframework.data.jpa.repository.JpaRepository

interface SocialRepository: JpaRepository<Social, Int> {
    fun findByEmailAndPlatform(email: String, platform: Platform): Social?

    fun existsByNickname(nickName: String): Boolean

    fun existsByEmailAndPlatform(email: String, platform: Platform): Boolean
}