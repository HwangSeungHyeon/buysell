//package com.teamsparta.buysell.domain.member.service
//
//
//import com.teamsparta.buysell.domain.member.dto.OAuth2UserInfo
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
//import org.springframework.security.oauth2.core.user.OAuth2User
//import org.springframework.stereotype.Service
//
//@Service
//class OAuth2UserService(
//    private val socialMemberService: SocialMemberService
//) : DefaultOAuth2UserService() {
//
//    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
//        val originUser = super.loadUser(userRequest)
//        val platform = userRequest.clientRegistration.clientName  // "KAKAO"
//        return OAuth2UserInfo.of(platform, userRequest, originUser)
//            .also { socialMemberService.registerIfAbsent(it) }
//    }
//}