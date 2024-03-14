package com.teamsparta.buysell.domain.member.model

import com.teamsparta.buysell.infra.auditing.SoftDeleteEntity
import jakarta.persistence.*

@Entity
@Table(name = "account")
class Account (

    @Column(name = "account_balance")
    var accountBalance: Long = 0
): SoftDeleteEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    fun depositToAccount(money: Long){
        accountBalance += money
    }

    fun payment(price: Long){
        if(availableForPurchase(price)){ //결제 가능할 때
            accountBalance -= price
        } else{ //결제 불가능할 때 에러를 던진다
            throw IllegalArgumentException("잔액이 부족합니다.")
        }
    }

    fun availableForPurchase(price: Long): Boolean{
        return accountBalance > price
    }
    fun refundToAccount(money: Long) {
        accountBalance += money
    }
}
