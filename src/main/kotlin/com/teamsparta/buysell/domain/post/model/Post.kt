package com.teamsparta.buysell.domain.post.model

import com.teamsparta.buysell.domain.comment.dto.response.CommentResponse
import com.teamsparta.buysell.domain.comment.model.Comment
import com.teamsparta.buysell.domain.exception.ForbiddenException
import com.teamsparta.buysell.domain.member.model.Member
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

    @Column(name = "created_name")
    val createdName: String?,

    @Column(name = "price")
    var price: Int,

    @Column(name = "is_soldout")
    var isSoldOut: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val comment: MutableList<Comment> = mutableListOf(),
//    @Column(name = "is_deleted")
//    var isDeleted: Boolean = false,

    @Column(name = "view")
    var view: Int = 0,

//    @Column(name = "created_at")
//    val createdAt: LocalDateTime = LocalDateTime.now(),
//
//    @Column(name = "updated_at")
//    val updatedAt: LocalDateTime = LocalDateTime.now(),

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

    fun myPostCheckPermission(
        principal: UserPrincipal
    ){
        if(member.id != principal.id)
            throw ForbiddenException("내 게시물에 리뷰를 작성할 수 없습니다.")
    }
}

fun Post.toResponse(): PostResponse {
    return PostResponse(
        id = id!!,
        title = title,
        content = content,
        createdName = createdName, // member name 으로 수정
        price = price,
        isSoldout = isSoldOut,
        comment = comment
            .filter { !it.isDeleted }
            .map { CommentResponse.toResponse(it) }
//        category = category
    )
}