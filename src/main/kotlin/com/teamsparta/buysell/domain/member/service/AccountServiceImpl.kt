package com.teamsparta.buysell.domain.member.service

import com.teamsparta.buysell.domain.exception.ModelNotFoundException
import com.teamsparta.buysell.domain.member.dto.response.AccountResponse
import com.teamsparta.buysell.domain.member.repository.AccountRepository
import com.teamsparta.buysell.domain.member.repository.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AccountServiceImpl(
    private val accountRepository: AccountRepository,
    private val memberRepository: MemberRepository
): AccountService{
    override fun getMyAccount(memberId: Int): AccountResponse {
        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw ModelNotFoundException("Member", memberId)

        return AccountResponse(member.account.accountBalance)

    }

    @Transactional
    override fun chargeAccount(money: Int, memberId: Int): AccountResponse{
        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw ModelNotFoundException("Member", memberId)

        member.account.apply {
            modifyBalance(money)
        }

        return AccountResponse(member.account.accountBalance)
    }
}