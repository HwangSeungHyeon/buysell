package com.teamsparta.buysell.domain.post.repository

import com.teamsparta.buysell.domain.member.model.Member
import com.teamsparta.buysell.domain.post.model.WishList
import org.springframework.data.jpa.repository.JpaRepository

interface WishListRepository: JpaRepository<WishList, Int> {

    fun existsByPostIdAndMemberId(postId: Int, memberId: Int): Boolean

   // fun findByPostIdAndMemberId(postId: Int, memberId: Int): WishList?
    fun <T> findByPostIdAndMemberId(postId: Int, memberId: Int, type: Class<T>): T?

    fun deleteByPostIdAndMemberId(postId: Int, memberId: Int)

    fun findByMember(member: Member): List<WishList>
}