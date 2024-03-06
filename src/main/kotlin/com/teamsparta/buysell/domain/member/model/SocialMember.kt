//package com.teamsparta.buysell.domain.member.model
//
//import jakarta.persistence.Column
//import jakarta.persistence.Entity
//import jakarta.persistence.EnumType
//import jakarta.persistence.Enumerated
//import jakarta.persistence.GeneratedValue
//import jakarta.persistence.GenerationType
//import jakarta.persistence.Id
//
//@Entity(name="social")
//class SocialMember(
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "social_member_id")
//    var id: Long? = null,
//
//    val platformId: String,
//    val nickname: String,
//    @Enumerated(EnumType.STRING)
//    val platform: Platform,
//
//) {
//    companion object{
//        fun ofKakao(id: String, nickname: String):SocialMember{
//            return SocialMember(
//                platform = Platform.KAKAO,
//                platformId = id,
//                nickname = nickname,
//            )
//        }
//    }
//
//}