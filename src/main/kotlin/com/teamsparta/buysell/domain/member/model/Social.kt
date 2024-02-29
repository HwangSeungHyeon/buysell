package com.teamsparta.buysell.domain.member.model

import com.teamsparta.buysell.domain.member.dto.response.MemberResponse
import com.teamsparta.buysell.domain.member.dto.response.SocialResponse
import jakarta.persistence.*

@Entity(name = "member")
class Social(
    @Column(name = "email")
    val email : String,
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

    fun toResponse(): SocialResponse {
        return SocialResponse(
            id = id!!,
            email = email,
            role = role,
            platform = platform,
        )
    }
}