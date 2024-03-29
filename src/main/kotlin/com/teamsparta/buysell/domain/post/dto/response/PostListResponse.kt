package com.teamsparta.buysell.domain.post.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(
    description = "게시글 목록을 조회할 때, 필요한 내용만을 전달하는 객체." +
            "상세 조회면 몰라도 게시글 목록을 조회할 때는 " +
            "Post 테이블에 있는 내용을 전부 가지고 올 필요가 없기 때문에 별개로 만든 객체." +
            "QueryDsl 에서 Projection 으로 사용한다."
)
data class PostListResponse(
    val id: Int?,
    val title: String,
    val createdName: String?,
    val price: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val view: Int,
    val imageUrl: String?,
    val isSoldOut: Boolean
)
