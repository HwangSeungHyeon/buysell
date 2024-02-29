package com.teamsparta.buysell.domain.post.model

import com.teamsparta.buysell.domain.exception.ForbiddenException
import com.teamsparta.buysell.domain.member.model.Member
import com.teamsparta.buysell.infra.security.UserPrincipal
import jakarta.persistence.*

@Table(name = "post_likes")
@Entity
class Like private constructor(
    @ManyToOne
    @JoinColumn(name = "post_id")
    var post: Post,

    @ManyToOne
    @JoinColumn(name = "member_id")
    var member: Member,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null

    companion object{
        fun makeEntity(
            member: Member,
            post: Post
        ): Like{
            return Like(
                member = member,
                post = post
            )
        }

        fun checkPermission(
            post: Post,
            userPrincipal: UserPrincipal
        ){
            if(post.id == userPrincipal.id)
                throw ForbiddenException("자신이 작성한 게시글에는 찜을 할 수 없습니다.")
        }
    }
}