package com.teamsparta.buysell.domain.post.model

import com.teamsparta.buysell.domain.exception.ForbiddenException
import com.teamsparta.buysell.domain.member.model.Member
import com.teamsparta.buysell.infra.security.UserPrincipal
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Table(name = "post_likes")
@Entity
class Like private constructor(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var post: Post,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
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
            if(post.member.id == userPrincipal.id)
                throw ForbiddenException("자신이 작성한 게시글에는 찜을 할 수 없습니다.")
        }
    }
}