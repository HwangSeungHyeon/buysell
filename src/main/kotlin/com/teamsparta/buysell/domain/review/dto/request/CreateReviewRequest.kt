package com.teamsparta.buysell.domain.review.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

@Schema(description = "리뷰를 작성할 때 입력한 정보를 전달하는 객체")
data class CreateReviewRequest(

    @field:NotBlank(message = "내용을 입력해주세요.")
    val content: String,

    @field:Min(value = 1, message = "별점은 1에서 5까지만 입력할 수 있습니다.")
    @field:Max(value = 5, message = "message = 별점은 1에서 5까지만 입력할 수 있습니다.")
    val rating: Int,
)