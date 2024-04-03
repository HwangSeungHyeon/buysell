package com.teamsparta.buysell.domain.post.model

import com.teamsparta.buysell.domain.comment.dto.response.CommentResponse
import com.teamsparta.buysell.domain.comment.model.Comment
import com.teamsparta.buysell.domain.exception.ForbiddenException
import com.teamsparta.buysell.domain.member.model.Member
import com.teamsparta.buysell.domain.order.model.Order
import com.teamsparta.buysell.domain.post.dto.request.UpdatePostRequest
import com.teamsparta.buysell.domain.post.dto.response.PostListResponse
import com.teamsparta.buysell.domain.post.dto.response.PostResponse
import com.teamsparta.buysell.infra.auditing.SoftDeleteEntity
import com.teamsparta.buysell.infra.security.UserPrincipal
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "post")
@SQLDelete(sql = "UPDATE post SET is_deleted = true WHERE id = ?") // DELETE 쿼리 대신 실행
@SQLRestriction("is_deleted = false")
class Post(
    @Column(name = "title")
    var title: String,

    @Column(name = "content")
    var content: String,

    @Column(name = "price")
    var price: Long,

    @Column(name = "is_soldout")
    var isSoldOut: Boolean = false,

    @OneToOne(mappedBy = "post", fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    var order: Order? = null,

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("createdAt ASC")
    val comment: MutableList<Comment> = mutableListOf(),

    @Column(name = "view")
    var view: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member,

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    var category: Category,

    @Column(name = "img_url")
    var imageUrl: String?,

    ) : SoftDeleteEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    fun checkPermission(
        principal: UserPrincipal
    ) {
        if (member.id != principal.id)
            throw ForbiddenException("권한이 없습니다.")
    }

    fun increaseView() = this.view++


    fun myPostCheckPermission(
        principal: UserPrincipal
    ) {
        if (member.id == principal.id)
            throw ForbiddenException("본인의 게시물에 이용할 수 없는 서비스입니다.")
    }

    fun outOfStockStatus() {
        if (isSoldOut) {
            throw IllegalStateException("판매완료된 게시글입니다.")
        }
    }

    fun postUpdate(
        request: UpdatePostRequest
    ) {
        this.title = request.title
        this.content = request.content
        this.price = request.price
        this.category = request.category
        this.imageUrl = request.imageUrl
    }

    fun toListResponse(): PostListResponse {
        return PostListResponse(
            id = id,
            title = title,
            createdName = member.nickname,
            price = price,
            createdAt = createdAt,
            updatedAt = updatedAt,
            view = view,
            imageUrl = imageUrl,
            isSoldOut = isSoldOut
        )
    }

    fun toResponse(): PostResponse {
        return PostResponse(
            id = id!!,
            memberId = member.id!!,
            title = title,
            content = content,
            createdName = member.nickname,
            price = price,
            isSoldout = isSoldOut,
            createdAt = createdAt,
            view = view,
            updatedAt = updatedAt,
            imageUrl = imageUrl,
            comment = comment
                .sortedBy { it.createdAt }
                .map { CommentResponse.toResponse(it) }
        )
    }
}



