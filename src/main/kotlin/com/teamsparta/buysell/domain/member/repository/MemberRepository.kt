package com.teamsparta.buysell.domain.member.repository

import com.teamsparta.buysell.domain.member.model.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository: JpaRepository<Member, Int> {
    fun findByEmail(email: String): Member?
}