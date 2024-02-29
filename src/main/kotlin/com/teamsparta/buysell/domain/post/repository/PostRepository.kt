package com.teamsparta.buysell.domain.post.repository

import com.teamsparta.buysell.domain.post.model.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Int>, CustomPostRepository {

}