package com.teamsparta.buysell.domain.member.model

import com.teamsparta.buysell.domain.member.dto.response.MemberResponse
import jakarta.persistence.*

@Table(name = "member")
@Entity
class Member(
    @Column(name = "email")
    val email : String,

    @Column(name = "password")
    val password : String?,

    @Column(name = "nickname")
    var nickname : String?,

    @Column(name = "gender")
    var gender : String?,

    @Column(name = "birthday")
    var birthday : String?,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    val role : Role?,

    @Column(name = "platform")
    @Enumerated(EnumType.STRING)
    val platform : Platform?,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null

    fun toResponse():MemberResponse{
        return MemberResponse(
            id = id!!,
            email = email,
            nickname = nickname,
            role = role,
            gender = gender,
            birthday = birthday,
            platform = platform,
        )
    }
}

