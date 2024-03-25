package com.teamsparta.buysell.domain.review.model

import com.teamsparta.buysell.domain.exception.ForbiddenException
import com.teamsparta.buysell.domain.member.model.Member
import com.teamsparta.buysell.domain.post.model.Post
import com.teamsparta.buysell.domain.review.dto.request.CreateReviewRequest
import com.teamsparta.buysell.domain.review.dto.request.UpdateReviewRequest
import com.teamsparta.buysell.domain.review.dto.response.ReviewResponse
import com.teamsparta.buysell.infra.auditing.SoftDeleteEntity
import com.teamsparta.buysell.infra.security.UserPrincipal
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "review")
@SQLDelete(sql = "UPDATE review SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
class Review private constructor(

    @Column(name = "content")
    var content: String,

    @Column(name = "rating")
    var rating: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    val post: Post,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,
) : SoftDeleteEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    fun checkPermission(
        principal: UserPrincipal
    ) {
        if (member.id != principal.id)
            throw ForbiddenException("수정 권한이 없습니다.")
    }

    fun editReview(
        request: UpdateReviewRequest
    ) {
        this.content = request.content
    }

    companion object {
        fun makeEntity(
            request: CreateReviewRequest,
            member: Member,
            post: Post
        ): Review {
            return Review(
                content = request.content,
                rating = request.rating,
                post = post,
                member = member
            )
        }
    }
}

fun Review.toResponse(): ReviewResponse {
    return ReviewResponse(
        nickname = member.nickname,
        rating = rating,
        content = content
    )
}

