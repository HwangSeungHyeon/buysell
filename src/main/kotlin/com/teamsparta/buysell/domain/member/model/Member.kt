package com.teamsparta.buysell.domain.member.model

import com.teamsparta.buysell.domain.member.dto.response.MemberResponse
import jakarta.persistence.*

@Entity
@Table(name = "member")
class Member(
    @Column(name = "email")
    val email : String,

    @Column(name = "password")
    val password : String?,

    @Column(name = "nickname")
    val nickname : String?,

    @Column(name = "gender")
    val gender : String?,

    @Column(name = "birthday")
    val birthday : String?,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    val role : Role?,

    @Column(name = "platform")
    @Enumerated(EnumType.STRING)
    val platform : Platform?,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

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

