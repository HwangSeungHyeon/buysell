//package com.teamsparta.buysell.domain.member.dto
//
//import org.springframework.security.core.GrantedAuthority
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
//import org.springframework.security.oauth2.core.user.OAuth2User
//
//data class OAuth2UserInfo(
//    val id: String,
//    val platform: String,
//    val nickname: String,
//): OAuth2User {
//    override  fun getName(): String{
//        return "$platform:$id"
//    }
//
//    override fun getAttributes(): MutableMap<String, Any> {
//        return mutableMapOf()
//    }
//
//    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
//        return mutableListOf()
//    }
//
//    companion object{
//        fun of(platform: String, userRequest: OAuth2UserRequest, originUser: OAuth2User ): OAuth2UserInfo{
//            return when(platform){
//                "KAKAO", "kakao" -> ofKakao(platform, userRequest, originUser)
//                else -> throw RuntimeException("지원하지 않는 OAuth Platform 입니다.")
//            }
//        }
//        private fun ofKakao(platform: String, userRequest: OAuth2UserRequest, originUser: OAuth2User): OAuth2UserInfo{
//            val profile = originUser.attributes["properties"] as Map<*,*>
//            val userNameAttributeName = userRequest.clientRegistration.providerDetails.userInfoEndpoint.userNameAttributeName
//            val nickname = profile["nickname"] ?: ""
//
//            return OAuth2UserInfo(
//                id = (originUser.attributes[userNameAttributeName] as Long).toString(),
//                platform = platform.uppercase(),
//                nickname = nickname as String,
//            )
//        }
//    }
//}