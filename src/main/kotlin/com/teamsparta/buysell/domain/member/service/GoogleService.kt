package com.teamsparta.buysell.domain.member.service

import com.teamsparta.buysell.domain.member.model.Platform
import com.teamsparta.buysell.domain.member.model.Role
import com.teamsparta.buysell.domain.member.model.Social
import com.teamsparta.buysell.domain.member.repository.SocialRepository
import com.teamsparta.buysell.infra.social.jwt.JwtDto
import com.teamsparta.buysell.infra.social.jwt.JwtProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class GoogleService(
    private val socialRepository: SocialRepository,
    //private val socialMemberRepository: SocialMemberRepository,
    private val jwtProvider: JwtProvider,

) {
    fun getGoogleLoginPage(): String {
        return "http://localhost:8080/oauth2/authorization/google"
    }

    fun googleLogin(oAuth2User: OAuth2User) : JwtDto {
        val email = oAuth2User.attributes.get("email").toString()

        val platform = Platform.GOOGLE
        val role = Role.MEMBER
        val member = if(!socialRepository.existsByEmail(email)) {
            val newMember = Social(
                email = email,
                role = role,
                platform = platform
            )
            socialRepository.save(newMember)
            newMember
        } else {
            socialRepository.findByEmail(email) ?: throw BadCredentialsException("User with email $email not found")
        }

        return jwtProvider.generateJwtDto(oAuth2User, member.id.toString(), role.name, platform)
    }

    fun getKakaoLoginPage(): String{
        return "http://localhost:8080/oauth2/authorization/kakako"
    }

    fun kakaoLogin(oAuth2User: OAuth2User) : JwtDto {
        val email = oAuth2User.attributes.get("email").toString()

        val platform = Platform.KAKAO
        val role = Role.MEMBER
        val member = if(!socialRepository.existsByEmail(email)) {
            val newMember = Social(
                email = email,
                role = role,
                platform = platform
            )
            socialRepository.save(newMember)
            newMember
        } else {
            socialRepository.findByEmail(email) ?: throw BadCredentialsException("User with email $email not found")
        }

        return jwtProvider.generateJwtDto(oAuth2User, member.id.toString(), role.name, platform)
    }

//    fun getKakaoLoginPage(): String {
//        return "http://localhost:8080/oauth2/authorization/kakao"
//    }
//    fun kakaoLogin(oAuth2User: OAuth2User) : JwtDto {
//        val id = oAuth2User.attributes.get("id").toString()
//        val nickname = oAuth2User.attributes.get("properties.nickname").toString()
//
//        val platform = Platform.KAKAO
//        val role = Role.MEMBER
//        val member = if(!socialMemberRepository.existsByPlatformAndPlatformId(platform, id)) {
//            val newMember = SocialMember.ofKakao(id, nickname)
//            socialMemberRepository.save(newMember)
//            newMember
//        } else {
//            socialMemberRepository.findByPlatformAndPlatformId(platform, id)
//        }
//
//        return jwtProvider.generateJwtDto(oAuth2User, member.id.toString(), role.name, platform)
//    }
}