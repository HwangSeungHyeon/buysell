package com.teamsparta.buysell.domain.post.model

import com.teamsparta.buysell.domain.exception.ForbiddenException
import com.teamsparta.buysell.domain.member.model.Member
import com.teamsparta.buysell.infra.security.UserPrincipal
import jakarta.persistence.*

@Table(name = "wish_list")
@Entity
class WishList private constructor(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    var post: Post,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null

    companion object{
        fun makeEntity(
            member: Member,
            post: Post
        ): WishList{
            return WishList(
                member = member,
                post = post
            )
        }

        fun checkPermission(
            post: Post,
            userPrincipal: UserPrincipal
        ){
            if(post.member.id == userPrincipal.id)
                throw ForbiddenException("자신이 작성한 게시글은 위시리스트에 추가할 수 없습니다.")
        }
    }
}