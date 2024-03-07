package com.teamsparta.buysell.domain.post.model

import com.teamsparta.buysell.domain.exception.ForbiddenException
import com.teamsparta.buysell.domain.member.model.Member
import com.teamsparta.buysell.domain.post.dto.response.PostResponse
import com.teamsparta.buysell.infra.auditing.SoftDeleteEntity
import com.teamsparta.buysell.infra.security.UserPrincipal
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete

//@SoftDelete(columnName = "is_deleted") //soft delete
@SQLDelete(sql = "UPDATE post SET is_deleted = true WHERE id = ?") // DELETE 쿼리 대신 실행
@Entity
@Table(name = "post")
class Post(
    @Column(name = "title")
    var title: String,

    @Column(name = "content")
    var content: String,

    @Column(name = "created_name")
    val createdName: String?,

    @Column(name = "price")
    var price: Int,

    @Column(name = "is_soldout")
    var isSoldOut: Boolean = false,

//    @Column(name = "is_deleted")
//    var isDeleted: Boolean = false,

    @Column(name = "view")
    var view: Int = 0,

//    @Column(name = "created_at")
//    val createdAt: LocalDateTime = LocalDateTime.now(),
//
//    @Column(name = "updated_at")
//    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member,

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    var category: Category

): SoftDeleteEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    fun checkPermission(
        principal: UserPrincipal
    ){
        if(member.id != principal.id)
            throw ForbiddenException("권한이 없습니다.")
    }

//    fun softDelete(){
//        this.isDeleted = true
//    }
}

fun Post.toResponse(): PostResponse {
    return PostResponse(
        id = id!!,
        title = title,
        content = content,
        createdName = createdName, // member name 으로 수정
        price = price,
        isSoldout = isSoldOut
//        category = category
    )
}