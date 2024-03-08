package com.teamsparta.buysell.domain.order.dto.request

data class CreateOrderRequest(
    val address: String,
    val phoneNumber: String
)
