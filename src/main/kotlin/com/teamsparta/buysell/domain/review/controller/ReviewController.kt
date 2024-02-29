package com.teamsparta.buysell.domain.review.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
@RequestMapping("/posts/{postId}/reviews")
@RestController
class ReviewController {

    @PostMapping
    fun createReview()
}