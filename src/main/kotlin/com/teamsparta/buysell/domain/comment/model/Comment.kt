package com.teamsparta.buysell.domain.comment.model

import com.teamsparta.buysell.domain.comment.dto.request.CreateRequest
import com.teamsparta.buysell.infra.auditing.BaseEntity
import jakarta.persistence.*

@Table(name = "comment")
@Entity
class Comment private constructor(
    @Column(name = "comment_content")
    val content: String,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var member: Member,

    @ManyToOne
    @JoinColumn(name = "post_id")
    var post: Post

) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    @Column(name = "created_name")
    var createdName = "홍길동"

    companion object{
        fun makeEntity(
            request: CreateRequest,
            member: Member,
            post: Post
        ) : Comment {
            return Comment(
                content = request.content,
                member = Member,
                post = Post
            )
        }
    }
}