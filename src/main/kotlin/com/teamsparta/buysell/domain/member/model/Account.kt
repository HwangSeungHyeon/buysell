package com.teamsparta.buysell.domain.member.model

import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "account")
@SQLDelete(sql = "UPDATE account SET is_deleted = true WHERE id = ?") // DELETE 쿼리 대신 실행
@SQLRestriction("is_deleted = false")
class Account (
    @Column(name = "is_deleted")
    var isDeleted: Boolean = false,

    @Column(name = "account_balance")
    var accountBalance: Long = 0
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    fun depositToAccount(money: Long){
        accountBalance += money
    }

    fun withDrawMoney(money: Long){
        if (accountBalance < money)
            throw IllegalStateException("계좌에 있는 돈보다 많이 출금할 수 없습니다.")
        accountBalance -= money
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
