package com.teamsparta.buysell.domain.post.repository

import com.teamsparta.buysell.domain.post.model.Post
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class PostRepositoryByJPQL (private val entityManager: EntityManager) {
    fun findAllPostsByOrderId(orderId: Int?): List<Post> {
        val query = entityManager.createQuery(
            "SELECT p FROM Post p WHERE p.order.id = :orderId", Post::class.java
        )
        query.setParameter("orderId", orderId)
        return query.resultList
    }
}
