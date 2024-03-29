package com.teamsparta.buysell.domain.coupon.moedel

import com.teamsparta.buysell.domain.member.model.Member
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "Coupon")
class Coupon(
    @Column(name = "content")
    var content: String,

    @Column(name = "coupon_number")
    val couponNumber: String,

    @Column(name = "coupon_count")
    val couponCount: Int,

    @Column(name = "coupon_exp")
    val couponExp: LocalDateTime? = null,

    @ManyToOne
    @JoinColumn(name = "member_id")
    var memberId: Member,

    @Column(name = "available")
    var available: Boolean,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
}