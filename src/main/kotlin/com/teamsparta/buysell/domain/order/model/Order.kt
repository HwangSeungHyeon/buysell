package com.teamsparta.buysell.domain.order.model

import com.teamsparta.buysell.domain.member.model.Member
import com.teamsparta.buysell.domain.post.model.Post
import jakarta.persistence.*
import java.time.LocalDateTime

@Table(name = "orders")
@Entity
class Order (

    @Column(name = "address")
    var address: String,

    @Column(name = "phone_number")
    var phoneNumber: String,

    @Column(name = "purchase_date")
    var purchaseDate: LocalDateTime,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    @OneToOne(fetch = FetchType.LAZY )
    var post: Post

){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
}