package com.teamsparta.buysell.domain.order.dto.request

data class CreateOrderRequest(
    val buyerName: String,
    val address: String,
    val phoneNumber: String
)
