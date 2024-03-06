//package com.teamsparta.buysell.domain.member.repository
//
//import com.teamsparta.buysell.domain.member.model.Platform
//import com.teamsparta.buysell.domain.member.model.SocialMember
//import org.springframework.data.repository.CrudRepository
//import org.springframework.stereotype.Repository
//
//@Repository
//interface SocialMemberRepository : CrudRepository<SocialMember,Long> {
//    fun existsByPlatformAndPlatformId(platform: Platform, id: String): Boolean
//    fun findByPlatformAndPlatformId(platform: Platform, id: String): SocialMember
//}