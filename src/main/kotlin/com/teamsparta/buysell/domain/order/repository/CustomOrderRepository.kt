package com.teamsparta.buysell.domain.order.repository

import com.teamsparta.buysell.domain.order.model.Order
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class CustomOrderRepository(private val entityManager: EntityManager) {
    fun findOrdersByMemberId(memberId: Int): List<Order> {
        val query = entityManager.createQuery(
            "SELECT o FROM Order o WHERE o.member.id = :memberId", Order::class.java
        )
        query.setParameter("memberId", memberId)
        return query.resultList
    }
}
