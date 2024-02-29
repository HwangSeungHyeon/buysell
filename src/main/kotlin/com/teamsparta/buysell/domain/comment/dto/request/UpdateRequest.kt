package com.teamsparta.buysell.domain.comment.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "댓글을 수정할 때 입력한 정보를 전달하는 객체")
data class UpdateRequest(
    @field:NotBlank(message = "댓글을 입력해주세요.")
    @field:Size(min = 1, max = 300, message = "댓글은 1자 이상 300자 이하로 작성해주세요")
    @Schema(description = "수정한 댓글 내용", example = "수정한 댓글 내용")
    val content: String
)