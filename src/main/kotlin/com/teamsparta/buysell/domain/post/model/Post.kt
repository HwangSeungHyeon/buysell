package com.teamsparta.buysell.domain.post.model

import com.teamsparta.buysell.domain.comment.dto.response.CommentResponse
import com.teamsparta.buysell.domain.comment.model.Comment
import com.teamsparta.buysell.domain.exception.ForbiddenException
import com.teamsparta.buysell.domain.member.model.Member
import com.teamsparta.buysell.domain.order.model.Order
import com.teamsparta.buysell.domain.post.dto.response.PostResponse
import com.teamsparta.buysell.infra.auditing.SoftDeleteEntity
import com.teamsparta.buysell.infra.security.UserPrincipal
import jakarta.persistence.*


@Entity
@Table(name = "post")
class Post(
    @Column(name = "title")
    var title: String,

    @Column(name = "content")
    var content: String,

    @Column(name = "price")
    var price: Int,

    @Column(name = "is_soldout")
    var isSoldOut: Boolean = false,

    @OneToOne(mappedBy = "post", fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    var order: Order? = null,

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val comment: MutableList<Comment> = mutableListOf(),

    @Column(name = "view")
    var view: Int = 0,

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
}

fun Post.toResponse(): PostResponse {
    return PostResponse(
        id = id!!,
        title = title,
        content = content,
        createdName = member.nickname,
        price = price,
        isSoldout = isSoldOut,
        comment = comment
            .filter { !it.isDeleted }
            .map { CommentResponse.toResponse(it) }
    )
}