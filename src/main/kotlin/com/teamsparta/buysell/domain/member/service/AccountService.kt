package com.teamsparta.buysell.domain.member.service

import com.teamsparta.buysell.domain.common.dto.MessageResponse
import com.teamsparta.buysell.domain.member.dto.response.AccountResponse

interface AccountService {
    fun getMyAccount(memberId: Int): AccountResponse

    fun chargeAccount(money: Long, memberId: Int): MessageResponse

    fun withDraw(money: Long, memberId: Int): MessageResponse
}
