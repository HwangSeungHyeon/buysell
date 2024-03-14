package com.teamsparta.buysell.domain.member.service

import com.teamsparta.buysell.domain.member.model.Account
import com.teamsparta.buysell.domain.member.model.Platform
import com.teamsparta.buysell.domain.member.model.Role
import com.teamsparta.buysell.domain.member.model.Social
import com.teamsparta.buysell.domain.member.repository.SocialRepository
import com.teamsparta.buysell.infra.social.jwt.JwtDto
import com.teamsparta.buysell.infra.social.jwt.JwtProvider
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service


@Service
class SocialService(
    private val socialRepository: SocialRepository,
    private val jwtProvider: JwtProvider,

) {
    fun getGoogleLoginPage(): String {
        return "http://localhost:8080/oauth2/authorization/google"
    }

    fun googleLogin(oAuth2User: OAuth2User) : JwtDto {
        val email = oAuth2User.attributes.get("email").toString()
        val nickname = oAuth2User.attributes.get("name").toString()
        val platform = Platform.GOOGLE
        val role = Role.MEMBER
        val member = socialRepository.findByEmailAndPlatform(email, platform) ?: run {
            val newMember = Social(
                email = email,
                nickname = nickname,
                role = role,
                platform = platform,
                account = Account()
            )
            socialRepository.save(newMember)
            newMember
        }

        return jwtProvider.generateJwtDto(oAuth2User, member.id.toString(), role.name, platform)
    }

    fun getKakaoLoginPage(): String{
        return "http://localhost:8080/oauth2/authorization/kakao"
    }

    fun kakaoLogin(oAuth2User: OAuth2User) : JwtDto {
        val kakaoAccount = oAuth2User.attributes["kakao_account"] as Map<*, *>
        val email = kakaoAccount["email"].toString()
        val profile = kakaoAccount["profile"] as Map<*, *>
        val nickname = profile["nickname"].toString()
        val platform = Platform.KAKAO
        val role = Role.MEMBER
        val member = socialRepository.findByEmailAndPlatform(email, platform) ?: run {
            val newMember = Social(
                email = email,
                nickname = nickname,
                role = role,
                platform = platform,
                account = Account()
            )
            socialRepository.save(newMember)
            newMember
        }
        return jwtProvider.generateJwtDto(oAuth2User, member.id.toString(), role.name, platform)
    }

    fun getNaverLoginPage(): String{
        return "http://localhost:8080/oauth2/authorization/naver"
    }

    fun naverLogin(oAuth2User: OAuth2User): JwtDto {
        val platform = Platform.NAVER
        val role = Role.MEMBER
        val attributes = oAuth2User.attributes["response"] as Map<*, *>
        val email = attributes["email"].toString()
        val nickname = attributes["nickname"].toString()
        val member = socialRepository.findByEmailAndPlatform(email, platform) ?: run {
            val newMember = Social(
                email = email,
                role = role,
                nickname = nickname,
                platform = platform,
                account = Account()
            )
            socialRepository.save(newMember)
            newMember
        }
        return jwtProvider.generateJwtDto(oAuth2User, member.id.toString(), role.name, platform)
    }
}