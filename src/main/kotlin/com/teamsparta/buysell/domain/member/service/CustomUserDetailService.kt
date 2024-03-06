package com.teamsparta.buysell.domain.member.service

import com.teamsparta.buysell.domain.member.model.Platform
import com.teamsparta.buysell.domain.member.model.Role
import com.teamsparta.buysell.domain.member.model.Social
import com.teamsparta.buysell.domain.member.repository.SocialRepository
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomUserDetailService(
    private val socialRepository: SocialRepository // Social 엔티티를 저장하거나 가져오는 데 사용하는 레포지토리
): DefaultOAuth2UserService() {

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)

        // 소셜 로그인 서비스로부터 받아온 사용자 정보
        val attributes = oAuth2User.attributes
        val email = attributes["email"] as String

        // 사용자 정보를 데이터베이스에 저장하거나 업데이트
        val social = socialRepository.findByEmail(email) ?: Social(
            email = email,
            role = Role.MEMBER,
            platform = Platform.valueOf(userRequest.clientRegistration.registrationId.uppercase(Locale.getDefault()))
        )
        socialRepository.save(social)

        return oAuth2User
    }
}