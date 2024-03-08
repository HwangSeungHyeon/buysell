package com.teamsparta.buysell.domain.member.model

import com.teamsparta.buysell.domain.comment.model.Comment
import com.teamsparta.buysell.domain.order.model.Order
import com.teamsparta.buysell.domain.post.model.Post
import com.teamsparta.buysell.infra.auditing.SoftDeleteEntity
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete

@Entity
@Table(name = "member")
@Schema(description = "소셜 회원 정보")
@SQLDelete(sql = "UPDATE member SET is_deleted = true WHERE id = ?")
class Social(
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "account_id")
    var account: Account,
    @OneToMany(mappedBy = "member", targetEntity = Order::class)
    var order: Set<Order> = hashSetOf(),
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
    @Column(name = "email")
    val email : String?,
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    val role : Role?,
    @Column(name = "platform")
    @Enumerated(EnumType.STRING)
    val platform : Platform?,
) : SoftDeleteEntity(){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
}