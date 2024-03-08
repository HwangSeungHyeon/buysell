package com.teamsparta.buysell.domain.member.model

import com.teamsparta.buysell.domain.member.dto.response.MemberResponse
import com.teamsparta.buysell.domain.order.model.Order
import com.teamsparta.buysell.infra.auditing.SoftDeleteEntity
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete

@Entity
@Table(name = "member")
@Schema(description = "회원 정보")
@SQLDelete(sql = "UPDATE member SET is_deleted = true WHERE id = ?") // DELETE 쿼리 대신 실행
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

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "account_id")
    var account: Account,

    @OneToMany(mappedBy = "member", targetEntity = Order::class)
    var order: Set<Order> = hashSetOf(),

    @Column(name = "platform")
    @Enumerated(EnumType.STRING)
    val platform : Platform?,
) : SoftDeleteEntity() {
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

