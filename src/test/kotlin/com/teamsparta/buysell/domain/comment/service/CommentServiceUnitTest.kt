package com.teamsparta.buysell.domain.comment.service

import com.teamsparta.buysell.domain.comment.dto.request.CreateRequest
import com.teamsparta.buysell.domain.comment.dto.request.UpdateRequest
import com.teamsparta.buysell.domain.comment.model.Comment
import com.teamsparta.buysell.domain.comment.repository.CommentRepository
import com.teamsparta.buysell.domain.exception.ForbiddenException
import com.teamsparta.buysell.domain.exception.ModelNotFoundException
import com.teamsparta.buysell.domain.member.model.Account
import com.teamsparta.buysell.domain.member.model.Member
import com.teamsparta.buysell.domain.member.model.Platform
import com.teamsparta.buysell.domain.member.model.Role
import com.teamsparta.buysell.domain.member.repository.MemberRepository
import com.teamsparta.buysell.domain.post.model.Category
import com.teamsparta.buysell.domain.post.model.Post
import com.teamsparta.buysell.domain.post.repository.PostRepository
import com.teamsparta.buysell.infra.security.UserPrincipal
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull

@ExtendWith(MockKExtension::class)
class CommentServiceUnitTest: BehaviorSpec({
    extension(SpringExtension)

    afterContainer {
        clearAllMocks()
    }

    // 종속 repository mocking
    val commentRepository = mockk<CommentRepository>()
    val postRepository = mockk<PostRepository>()
    val memberRepository = mockk<MemberRepository>()

    // courseService 생성
    val commentService = CommentServiceImpl(commentRepository, postRepository, memberRepository)

    val postId = 1
    val postWriterId = 1
    val consumerId = 2
    val commentId = 1

    val postWriter = Member(
        email = "test@gmail.com",
        password = "!test123",
        nickname = "데네브",
        role = Role.MEMBER,
        gender = "남자",
        birthday = "2024-03-06",
        platform = Platform.LOCAL,
        account = Account()
    ).apply { id = postWriterId }

    val ipadPost = Post(
        title = "아이패드 프로 4세대 m2 11인치 128기가 스페이스그레이",
        content = "풀박스이고 25년 1월 4일까지 보증기간입니다\n" +
                "기스나 하자 없습니다\n" +
                "배터리 성능 100\n" +
                "인강용으로 사용했고 정말 깨끗하게 사용하였습니다\n" +
                "직거래는 삼송역 가능합니다\n" +
                "에눌 문의 정중히 사양합니다",
        view = 0,
        price = 950000,
        member = postWriter,
        category = Category.FOOD,
        imageUrl = ""
    ).apply { id = postId }

    val consumer = Member(
        email = "test2@gmail.com",
        password = "!test123",
        nickname = "알타이르",
        role = Role.MEMBER,
        gender = "남자",
        birthday = "2024-03-06",
        platform = Platform.LOCAL,
        account = Account(),
        order = setOf()
    ).apply { id = consumerId }

    val createRequest = CreateRequest("테스트용으로 사용할 댓글")
    val updateRequest = UpdateRequest("테스트용으로 사용할 댓글인데 수정도 했어")
    val comment = Comment.makeEntity(request = createRequest, member = consumer, post = ipadPost).apply { id = commentId }

    given("댓글을 작성하려고 할 때"){
        val principal = UserPrincipal(id = consumerId, email = "test2@gmail.com", role = setOf("MEMBER"), platform = "LOCAL")
        `when`("댓글을 달 게시글이 없거나 댓글 작성자 정보가 DB에 없으면"){
            then("ModelNotFound 에러가 발생해야 한다."){
                every { postRepository.findByIdOrNull(any()) } returns null
                every { memberRepository.findByIdOrNull(any()) } returns null

                shouldThrow<ModelNotFoundException> {
                    commentService.addComment(postId = postId, request = createRequest, principal = principal)
                }
            }
        }
        `when`("댓글을 달 게시글이 있고,댓 글 작성자 정보도 DB에 있으면"){
            then("댓글이 저장되어야 한다."){
                every { postRepository.findByIdOrNull(any()) } returns ipadPost
                every { memberRepository.findByIdOrNull(any()) } returns consumer
                every { commentRepository.save(any()) } returns comment

                val result = commentService.addComment(postId = postId, request = createRequest, principal = principal)

                result.message shouldBe "댓글이 작성되었습니다."
            }
        }
    }

    given("댓글을 수정하려고 할 때"){
        `when`("수정할 댓글이 없으면"){
            then("ModelNotFound 에러가 발생해야 한다."){
                val principal = UserPrincipal(id = consumerId, email = "test2@gmail.com", role = setOf("MEMBER"), platform = "LOCAL")
                every { commentRepository.findByPostIdAndId(any(), any()) } returns null

                shouldThrow<ModelNotFoundException> {
                    commentService.editComment(postId = postId, commentId = commentId, request = updateRequest, principal = principal)
                }
            }
        }

        `when`("수정할 권한이 없으면"){
            then("ForbiddenException 에러가 발생해야 한다."){
                val principal = UserPrincipal(id = 5, email = "test2@gmail.com", role = setOf("MEMBER"), platform = "LOCAL")

                every { commentRepository.findByPostIdAndId(any(), any()) } returns comment

                val exception = shouldThrow<ForbiddenException> {
                    commentService.editComment(postId = postId, commentId = commentId, request = updateRequest, principal = principal)
                }
                exception.message shouldBe "수정 권한이 없습니다."
            }
        }

        `when`("수정할 권한이 있으면"){
            then("댓글이 수정되어야 한다."){
                val principal = UserPrincipal(id = consumerId, email = "test2@gmail.com", role = setOf("MEMBER"), platform = "LOCAL")

                every { commentRepository.findByPostIdAndId(any(), any()) } returns comment

                val result = commentService.editComment(postId = postId, commentId = commentId, request = updateRequest, principal = principal)
                result.message shouldBe "댓글이 수정되었습니다."
            }
        }
    }

    given("댓글을 삭제하려고 할 때"){
        `when`("삭제할 댓글이 없으면"){
            then("ModelNotFound 에러가 발생해야 한다."){
                val principal = UserPrincipal(id = consumerId, email = "test2@gmail.com", role = setOf("MEMBER"), platform = "LOCAL")
                every { commentRepository.findByPostIdAndId(any(), any()) } returns null

                shouldThrow<ModelNotFoundException> {
                    commentService.deleteComment(postId = postId, commentId = commentId, principal = principal)
                }
            }
        }

        `when`("삭제할 권한이 없으면"){
            then("ForbiddenException 에러가 발생해야 한다."){
                val principal = UserPrincipal(id = 5, email = "test2@gmail.com", role = setOf("MEMBER"), platform = "LOCAL")

                every { commentRepository.findByPostIdAndId(any(), any()) } returns comment

                val exception = shouldThrow<ForbiddenException> {
                    commentService.deleteComment(postId = postId, commentId = commentId, principal = principal)
                }
                exception.message shouldBe "수정 권한이 없습니다."
            }
        }

        `when`("삭제할 권한이 있으면"){
            then("댓글이 삭제되어야 한다."){
                val principal = UserPrincipal(id = consumerId, email = "test2@gmail.com", role = setOf("MEMBER"), platform = "LOCAL")

                every { commentRepository.findByPostIdAndId(any(), any()) } returns comment
                every { commentRepository.delete(any()) } returns Unit

                val result = commentService.deleteComment(postId = postId, commentId = commentId, principal = principal)
                result.message shouldBe "댓글이 삭제되었습니다."
            }
        }
    }

})