package com.teamsparta.buysell.domain.member.repository

import com.teamsparta.buysell.domain.member.model.Member
import com.teamsparta.buysell.domain.member.model.Platform
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository: JpaRepository<Member, Int> {
    fun findByEmail(email: String): Member?
    fun findByEmailAndPlatform(email:String, platform: Platform): Member?
    fun findByNickname(nickname: String): Member?
    fun findByEmailAndIsVerified(email: String, isVerified: Boolean): Member?
}