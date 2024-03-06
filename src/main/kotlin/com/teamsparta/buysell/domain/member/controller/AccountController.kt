package com.teamsparta.buysell.domain.member.controller

import com.teamsparta.buysell.domain.member.dto.response.AccountResponse
import com.teamsparta.buysell.domain.member.service.AccountService
import com.teamsparta.buysell.infra.security.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
@RequestMapping("/accounts")
@RestController
class AccountController(
    private val accountService: AccountService
) {

    // 계좌 조회
    @GetMapping
    fun getMyAccount(
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<AccountResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(accountService.getMyAccount(userPrincipal.id))
    }

    @PatchMapping
    fun chargeAccount(
        @RequestParam money:Int,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<AccountResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(accountService.chargeAccount(money, userPrincipal.id))
    }
}