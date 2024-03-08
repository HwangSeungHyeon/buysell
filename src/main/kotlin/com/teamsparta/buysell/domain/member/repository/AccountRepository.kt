package com.teamsparta.buysell.domain.member.repository

import com.teamsparta.buysell.domain.member.model.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, Int> {

//    fun findByMemberId(memberId: Int): Account?
}