package com.teamsparta.buysell.domain.order.repository

import com.teamsparta.buysell.domain.order.model.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository: JpaRepository<Order, Int> {

    fun findByPostIdAndMemberId(postId: Int, memberId: Int): Order?
    fun findByMemberId(memberId: Int): List<Order>
}