package com.teamsparta.buysell.domain.member.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "account")
class Account (

    @Column(name = "account_balance")
    var accountBalance: Int = 0

//    @OneToOne(mappedBy = "member")
//    val member: Member
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    fun modifyBalance(money: Int){
        accountBalance += money
    }
}