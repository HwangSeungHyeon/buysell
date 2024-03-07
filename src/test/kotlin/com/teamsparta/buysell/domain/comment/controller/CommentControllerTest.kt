//package com.teamsparta.buysell.domain.comment.controller
//
//import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
//import com.teamsparta.buysell.domain.comment.service.CommentService
//import com.teamsparta.buysell.domain.common.dto.MessageResponse
//import com.teamsparta.buysell.infra.security.jwt.JwtPlugin
//import io.kotest.core.spec.style.DescribeSpec
//import io.kotest.extensions.spring.SpringExtension
//import io.kotest.matchers.shouldBe
//import io.mockk.every
//import io.mockk.junit5.MockKExtension
//import io.mockk.mockk
//import org.junit.jupiter.api.extension.ExtendWith
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.test.web.servlet.MockMvc
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ExtendWith(MockKExtension::class)
//class CommentControllerTest @Autowired constructor(
//    private val mockMvc: MockMvc,
//    private val jwtPlugin: JwtPlugin
//) : DescribeSpec({
//    extension(SpringExtension)
//
//    val commentService = mockk<CommentService>()
//
//
//    describe("POST /posts/{postId}/comments"){
//        context("댓글 작성 요청을 보낼 때"){
//            it("201 status code를 응답한다."){
//
//                every { commentService.addComment(  ) } returns MessageResponse("댓글이 작성되었습니다.")
//
//
//                val jwtToken = jwtPlugin.generateAccessToken()
//
//                val result = mockMvc.perform(
//
//                ).andReturn()
//
//                result.response.status shouldBe 201
//            }
//        }
//    }
//})