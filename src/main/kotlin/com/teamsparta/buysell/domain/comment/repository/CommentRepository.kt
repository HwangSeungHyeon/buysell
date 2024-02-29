package com.teamsparta.buysell.domain.comment.repository

import com.teamsparta.buysell.domain.comment.model.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository: JpaRepository<Comment, Int> {

}