package com.teamsparta.buysell.domain.order.controller

import com.teamsparta.buysell.domain.common.dto.MessageResponse
import com.teamsparta.buysell.domain.order.dto.request.CreateOrderRequest
import com.teamsparta.buysell.domain.order.service.OrderService
import com.teamsparta.buysell.infra.security.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RequestMapping("/posts/{postId}/orders")
@RestController
class OrderController(
    private val orderService: OrderService
) {
    @PreAuthorize("hasRole('ADMIN') OR hasRole('MEMBER')")
    @PostMapping
    fun createOrder(
        @PathVariable postId: Int,
        @RequestBody createOrderRequest: CreateOrderRequest,
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<MessageResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(orderService.createOrder(postId, createOrderRequest, principal))
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('MEMBER')")
    @DeleteMapping("/{orderId}")
    fun cancelOrder(
        @PathVariable postId: Int,
        @PathVariable orderId: Int,
        @AuthenticationPrincipal principal: UserPrincipal,
    ): ResponseEntity<MessageResponse> {

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(orderService.cancelOrder(postId, orderId, principal))
    }
}