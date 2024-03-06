//package com.teamsparta.buysell.domain.member.service
//
//import com.teamsparta.buysell.domain.member.dto.OAuth2UserInfo
//import com.teamsparta.buysell.domain.member.model.Platform
//import com.teamsparta.buysell.domain.member.model.SocialMember
//import com.teamsparta.buysell.domain.member.repository.SocialMemberRepository
//import org.springframework.stereotype.Service
//
//@Service
//class SocialMemberService(
//    private val socialMemberRepository: SocialMemberRepository
//) {
//
//    fun registerIfAbsent(userInfo: OAuth2UserInfo): SocialMember {
//        val platform = Platform.valueOf(userInfo.platform)
//        return if (!socialMemberRepository.existsByPlatformAndPlatformId(platform, userInfo.id)) {
//            val socialMember = SocialMember.ofKakao(userInfo.id, userInfo.nickname)
//            socialMemberRepository.save(socialMember)
//        } else {
//            socialMemberRepository.findByPlatformAndPlatformId(platform, userInfo.id)
//        }
//    }
//}