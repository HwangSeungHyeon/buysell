package com.teamsparta.buysell.domain.post.repository

import com.teamsparta.buysell.domain.member.model.Member
import com.teamsparta.buysell.domain.post.model.Like
import org.springframework.data.jpa.repository.JpaRepository

interface LikeRepository: JpaRepository<Like, Int> {

    fun existsByPostIdAndMemberId(postId: Int, memberId: Int): Boolean

    fun findByPostIdAndMemberId(postId: Int, memberId: Int): Like?

    fun deleteByPostIdAndMemberId(postId: Int, memberId: Int)

    fun findByMember(member: Member): List<Like>

}