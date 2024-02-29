package com.teamsparta.buysell.domain.post.model

import com.teamsparta.buysell.domain.post.dto.response.PostResponse
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "post")
class Post(
    @Column(name = "title")
    var title: String,

    @Column(name = "content")
    var content: String,

    @Column(name = "created_name")
    val createdName: String,

    @Column(name = "price")
    var price: Int,

    @Column(name = "is_soldout")
    var isSoldOut: Boolean = false,

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false,

    @Column(name = "view")
    var view: Int = 0,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now(),

//    @ManyToOne
//    @JoinColumn(name = "member_id", nullabe = false)
//    var member: Member,

//    @Enumerated(EnumType.STRING)
//    @Column(name = "category")
//    var category: Category
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
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