package com.teamsparta.buysell.domain.member.service

import com.teamsparta.buysell.domain.member.model.Account
import com.teamsparta.buysell.domain.member.model.Platform
import com.teamsparta.buysell.domain.member.model.Role
import com.teamsparta.buysell.domain.member.model.Social
import com.teamsparta.buysell.domain.member.repository.SocialRepository
import com.teamsparta.buysell.infra.security.jwt.JwtPlugin
import com.teamsparta.buysell.infra.security.jwt.JwtDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import java.util.UUID


@Service
class SocialService(
    private val socialRepository: SocialRepository,
    private val jwtPlugin: JwtPlugin,
    @Value("\${app.baseUrl}") private val baseUrl: String
) {
    fun getGoogleLoginPage(): String {
        return "$baseUrl/oauth2/authorization/google"
    }
    fun getKakaoLoginPage(): String{
        return "$baseUrl/oauth2/authorization/kakao"
    }
    fun getNaverLoginPage(): String{
        return "$baseUrl/oauth2/authorization/naver"
    }

    fun googleLogin(oAuth2User: OAuth2User) : JwtDto {
        val email = oAuth2User.attributes.get("email").toString()

        if(socialRepository.existsByEmailAndPlatform(email, Platform.GOOGLE)) {
            throw DataIntegrityViolationException("이미 가입한 계정이 존재합니다.")
        }

        var nickname = makeNickName()

        while (socialRepository.existsByNickname(nickname)){
            nickname = makeNickName()
        }

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

        if(socialRepository.existsByEmailAndPlatform(email, Platform.KAKAO)) {
            throw DataIntegrityViolationException("이미 가입한 계정이 존재합니다.")
        }

        var nickname = makeNickName()

        while (socialRepository.existsByNickname(nickname)){
            nickname = makeNickName()
        }

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

        if(socialRepository.existsByEmailAndPlatform(email, Platform.NAVER)) {
            throw DataIntegrityViolationException("이미 가입한 계정이 존재합니다.")
        }

        var nickname = makeNickName()

        while (socialRepository.existsByNickname(nickname)){
            nickname = makeNickName()
        }
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

    private fun makeNickName(): String{
        val uuid = UUID.randomUUID().toString().split("-")
        val nickName = "Guest - " + uuid[0]
        return nickName
    }
}