package com.teamsparta.buysell.domain.member.model

import com.teamsparta.buysell.domain.comment.model.Comment
import com.teamsparta.buysell.domain.member.dto.response.MemberResponse
import com.teamsparta.buysell.domain.post.model.Post
import org.hibernate.annotations.SQLDelete
import com.teamsparta.buysell.domain.order.model.Order
import com.teamsparta.buysell.infra.auditing.SoftDeleteEntity
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*

@Entity
@Table(name = "member")
@Schema(description = "회원 정보")
@SQLDelete(sql = "UPDATE member SET is_deleted = true WHERE id = ?") // DELETE 쿼리 대신 실행
class Member(
    @Column(name = "email")
    val email : String,

    @Column(name = "password")
    var password : String?,

    @Column(name = "nickname", unique = true)
    var nickname : String?,

    @Column(name = "gender")
    var gender : String?,

    @Column(name = "birthday")
    var birthday : String?,

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

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    var status: MemberStatus = MemberStatus.NORMAL,

    @OneToMany(
        orphanRemoval = true,
        mappedBy = "member",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL]
    )
    var post : MutableList<Post> = mutableListOf(),
    @OneToMany(
        orphanRemoval = true,
        mappedBy = "member",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL]
    )
    var comment : MutableList<Comment> = mutableListOf(),

    @Column(name = "is_verified")
    var isVerified: Boolean = false
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

