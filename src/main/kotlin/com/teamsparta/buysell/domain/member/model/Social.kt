package com.teamsparta.buysell.domain.member.model

import jakarta.persistence.*

@Entity
@Table(name = "member")
class Social(
    @Column(name = "email")
    val email : String?,
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
}