package com.teamsparta.buysell.domain.order.service

import com.teamsparta.buysell.domain.common.dto.MessageResponse
import com.teamsparta.buysell.domain.order.dto.request.CreateOrderRequest
import com.teamsparta.buysell.infra.security.UserPrincipal

interface OrderService {

    fun createOrder(postId: Int, request: CreateOrderRequest, principal: UserPrincipal): MessageResponse

}
