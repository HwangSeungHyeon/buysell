package com.teamsparta.buysell.domain.comment.model

import com.teamsparta.buysell.domain.comment.dto.request.CreateRequest
import com.teamsparta.buysell.domain.comment.dto.request.UpdateRequest
import com.teamsparta.buysell.domain.exception.ForbiddenException
import com.teamsparta.buysell.domain.member.model.Member
import com.teamsparta.buysell.domain.post.model.Post
import com.teamsparta.buysell.infra.auditing.BaseEntity
import com.teamsparta.buysell.infra.auditing.SoftDeleteEntity
import com.teamsparta.buysell.infra.security.UserPrincipal
import jakarta.persistence.*

@Table(name = "comment")
@Entity
class Comment private constructor(
    @Column(name = "comment_content")
    var content: String,

    @Column(name = "created_name")
    var createdName: String,

//    @Column(name = "is_deleted")
//    var isDeleted: Boolean,

    @ManyToOne
    @JoinColumn(name = "member_id")
    var member: Member,

    @ManyToOne
    @JoinColumn(name = "post_id")
    var post: Post

) : SoftDeleteEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    fun edit(
        request: UpdateRequest
    ) {
        this.content = request.content
    }

    fun checkPermission(
        principal: UserPrincipal
    ){
        if(member.id != principal.id)
            throw ForbiddenException("수정 권한이 없습니다.")
    }

//    fun deleteComment(){
//        this.isDeleted = true
//    }

    companion object{
        fun makeEntity(
            request: CreateRequest,
            member: Member,
            post: Post
        ) : Comment {
            return Comment(
                content = request.content,
                createdName = member.nickname,
                member = member,
                post = post
            )
        }
    }
}