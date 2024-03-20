package com.teamsparta.buysell.domain.member.model

import com.teamsparta.buysell.domain.comment.model.Comment
import com.teamsparta.buysell.domain.member.dto.response.MemberResponse
import com.teamsparta.buysell.domain.order.model.Order
import com.teamsparta.buysell.domain.post.model.Post
import com.teamsparta.buysell.infra.auditing.SoftDeleteEntity
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "member")
@Schema(description = "회원 정보")
@SQLDelete(sql = "UPDATE member SET is_deleted = true WHERE id = ?") // DELETE 쿼리 대신 실행
@SQLRestriction("is_deleted = false")
class Member(
    @Column(name = "email")
    val email : String,

    @Column(name = "password")
    val password : String?,

    @Column(name = "nickname", unique = true)
    var nickname : String?,

    @Column(name = "gender")
    val gender : String?,

    @Column(name = "birthday")
    var birthday : String?,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    var role : Role?,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "account_id")
    var account: Account,

    @OneToMany(mappedBy = "member", targetEntity = Order::class)
    var order: Set<Order> = hashSetOf(),

    @Column(name = "platform")
    @Enumerated(EnumType.STRING)
    val platform : Platform?,

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

    @Column(name = "seller_rating")
    var sellerRating: Double? = null,

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
            gender = gender,
            birthday = birthday,
        )
    }
}

