package com.teamsparta.buysell.domain.order.model

import com.teamsparta.buysell.domain.member.model.Member
import com.teamsparta.buysell.domain.post.model.Post
import jakarta.persistence.*

@Table(name = "orders")
@Entity
class Order (

    @Column(name = "address")
    var address: String,

    @Column(name = "phone_number")
    var phoneNumber: String,

    @ManyToOne(fetch = FetchType.LAZY)
    val member: Member,

    @OneToOne(fetch = FetchType.LAZY )
    val post: Post,

    @Enumerated(EnumType.STRING)
    @Column(name = "order_state")
    var orderState: OrderState

){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
}