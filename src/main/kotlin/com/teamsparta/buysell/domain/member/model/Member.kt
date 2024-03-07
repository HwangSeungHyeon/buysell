package com.teamsparta.buysell.domain.member.model

import com.teamsparta.buysell.domain.comment.model.Comment
import com.teamsparta.buysell.domain.member.dto.response.MemberResponse
import com.teamsparta.buysell.domain.post.model.Post
import com.teamsparta.buysell.infra.auditing.SoftDeleteEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete

@Table(name = "member")
@Entity
@SQLDelete(sql = "UPDATE member SET is_deleted = true WHERE id = ?") // DELETE 쿼리 대신 실행
//@Where(clause = "is_deleted = false")
class Member(
    @Column(name = "email")
    val email : String,

    @Column(name = "password")
    val password : String?,

    @Column(name = "nickname")
    var nickname : String?,

    @Column(name = "gender")
    var gender : String?,

    @Column(name = "birthday")
    var birthday : String?,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    val role : Role?,

    @Column(name = "platform")
    @Enumerated(EnumType.STRING)
    val platform : Platform?,

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    var status: MemberStatus = MemberStatus.NORMAL,

    @OneToMany(
        orphanRemoval = true,
        mappedBy = "member",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL]
    )
    var post : MutableList<Post> = mutableListOf(),
    @OneToMany(
        orphanRemoval = true,
        mappedBy = "member",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL]
    )
    var comment : MutableList<Comment> = mutableListOf(),
) : SoftDeleteEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null

    fun toResponse():MemberResponse{
        return MemberResponse(
            id = id!!,
            email = email,
            nickname = nickname,
            role = role,
            gender = gender,
            birthday = birthday,
            platform = platform,
        )
    }
}

