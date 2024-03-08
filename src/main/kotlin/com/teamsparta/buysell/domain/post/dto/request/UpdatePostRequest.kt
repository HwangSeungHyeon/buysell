package com.teamsparta.buysell.domain.post.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdatePostRequest(
    @field:NotBlank(message = "제목을 입력해주세요.")
    @field:Size(min = 1, max = 50, message = "제목은 1자 이상 50자 이하로 작성해주세요")
    @Schema(description = "작성한 제목 내용", example = "제목 내용")
    val title: String,

    @field:NotBlank(message = "본문을 입력해주세요.")
    @Schema(description = "작성한 본문 내용", example = "본문 내용")
    val content: String,
    val price: Long,
//    val category: Category
)