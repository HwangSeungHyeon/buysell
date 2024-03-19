package com.teamsparta.buysell.domain.member.service

import com.teamsparta.buysell.domain.member.model.Account
import com.teamsparta.buysell.domain.member.model.Platform
import com.teamsparta.buysell.domain.member.model.Role
import com.teamsparta.buysell.domain.member.model.Social
import com.teamsparta.buysell.domain.member.repository.SocialRepository
import com.teamsparta.buysell.infra.security.jwt.JwtPlugin
import com.teamsparta.buysell.infra.security.jwt.JwtDto
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service


@Service
class SocialService(
    private val socialRepository: SocialRepository,
    private val jwtPlugin: JwtPlugin

) {
    fun getGoogleLoginPage(): String {
        return "http://localhost:8080/oauth2/authorization/google"
    }
    fun getKakaoLoginPage(): String{
        return "http://localhost:8080/oauth2/authorization/kakao"
    }
    fun getNaverLoginPage(): String{
        return "http://localhost:8080/oauth2/authorization/naver"
    }

    fun googleLogin(oAuth2User: OAuth2User) : JwtDto {
        val email = oAuth2User.attributes.get("email").toString()
        val nickname = oAuth2User.attributes.get("name").toString()
        val platform = Platform.GOOGLE
        val role = Role.MEMBER


        socialRepository.findByEmailAndPlatform(email, platform)
        val social =  Social(
            email = email,
            role = role,
            nickname = nickname,
            platform = platform,
            account = Account()
        )
        socialRepository.save(social)

        return jwtPlugin.generateJwtDto(oAuth2User, social.id.toString(), role.name, platform)
    }
    fun kakaoLogin(oAuth2User: OAuth2User) : JwtDto {
        val kakaoAccount = oAuth2User.attributes["kakao_account"] as Map<*, *>
        val email = kakaoAccount["email"].toString()
        val profile = kakaoAccount["profile"] as Map<*, *>
        val nickname = profile["nickname"].toString()
        val platform = Platform.KAKAO
        val role = Role.MEMBER
        socialRepository.findByEmailAndPlatform(email, platform)
        val social =  Social(
            email = email,
            role = role,
            nickname = nickname,
            platform = platform,
            account = Account()
        )
        socialRepository.save(social)
        return jwtPlugin.generateJwtDto(oAuth2User, social.id.toString(), role.name, platform)
    }
    fun naverLogin(oAuth2User: OAuth2User): JwtDto {
        val platform = Platform.NAVER
        val role = Role.MEMBER
        val attributes = oAuth2User.attributes["response"] as Map<*, *>
        val email = attributes["email"].toString()
        val nickname = attributes["nickname"].toString()
        socialRepository.findByEmailAndPlatform(email, platform)
            val social =  Social(
                email = email,
                role = role,
                nickname = nickname,
                platform = platform,
                account = Account()
             )
            socialRepository.save(social)

        return jwtPlugin.generateJwtDto(oAuth2User, social.id.toString(), role.name, platform)
    }
}