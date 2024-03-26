package com.teamsparta.buysell.domain.member.service

import com.teamsparta.buysell.domain.common.dto.MessageResponse
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
    override fun chargeAccount(money: Long, memberId: Int): MessageResponse{
        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw ModelNotFoundException("Member", memberId)

        member.account.apply {
            depositToAccount(money)
        }

        return MessageResponse("입금 되었습니다.")
    }

    @Transactional
    override fun withDraw(money: Long, memberId: Int): MessageResponse {
        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw ModelNotFoundException("Member", memberId)

        member.account.apply {
            withDrawMoney(money)
        }

        return MessageResponse("${money}원이 출금되었습니다.")
    }
}