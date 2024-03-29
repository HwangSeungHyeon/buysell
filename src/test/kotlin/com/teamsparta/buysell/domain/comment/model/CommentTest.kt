package com.teamsparta.buysell.domain.comment.model

import com.teamsparta.buysell.domain.comment.dto.request.CreateRequest
import com.teamsparta.buysell.domain.comment.dto.request.UpdateRequest
import com.teamsparta.buysell.domain.exception.ForbiddenException
import com.teamsparta.buysell.domain.member.model.Account
import com.teamsparta.buysell.domain.member.model.Member
import com.teamsparta.buysell.domain.member.model.Platform
import com.teamsparta.buysell.domain.member.model.Role
import com.teamsparta.buysell.domain.post.model.Category
import com.teamsparta.buysell.domain.post.model.Post
import com.teamsparta.buysell.infra.security.UserPrincipal
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class CommentTest: BehaviorSpec({
    extension(SpringExtension)

    val postWriter = Member(
        email = "test@gmail.com",
        password = "!test123",
        nickname = "데네브",
        role = Role.MEMBER,
        gender = "남자",
        birthday = "2024-03-06",
        platform = Platform.LOCAL,
        account = Account()
    ).apply { id = 1 }

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
    ).apply { id = 1 }

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
    ).apply { id = 2 }

    val createRequest = CreateRequest("댓글 테스트")

    given("Comment 객체가 없을 경우") {

        `when`("createRequest DTO가 Comment.makeEntity 메서드의 인자로 전달되었다면") {
            then("객체가 생성되어야 한다.") {
                val commentEntity = Comment.makeEntity(createRequest, consumer, ipadPost)

                commentEntity.id shouldBe null
                commentEntity.member.nickname shouldBe "알타이르"
                commentEntity.content shouldBe "댓글 테스트"
                commentEntity.post.title shouldBe "아이패드 프로 4세대 m2 11인치 128기가 스페이스그레이"
            }
        }
    }

    given("Comment 객체가 있을 경우") {
        val commentEntity = Comment.makeEntity(createRequest, consumer, ipadPost)

        `when`("updateRequest DTO가 edit 메서드의 인자로 전달되었다면") {
            val updateRequest = UpdateRequest("수정된 댓글 테스트")
            then("객체가 수정되어야 한다.") {
                commentEntity.edit(updateRequest)

                commentEntity.id shouldBe null
                commentEntity.member.nickname shouldBe "알타이르"
                commentEntity.content shouldBe "수정된 댓글 테스트"
                commentEntity.post.title shouldBe "아이패드 프로 4세대 m2 11인치 128기가 스페이스그레이"
            }
        }

        `when`("로그인한 사람과 댓글을 작성한 사람이 같은 사람일 경우") {
            val principal = UserPrincipal(id = 2, email = "test2@gmail.com", role = setOf("MEMBER"), platform = "LOCAL")

            then("아무런 값도 반환하지 않는다.") {
                commentEntity.checkPermission(principal)

                commentEntity.member.id shouldBe 2
            }
        }

        `when`("로그인한 사람과 댓글을 작성한 사람이 다른 사람일 경우") {
            val principal = UserPrincipal(id = 9999, email = "test2@gmail.com", role = setOf("MEMBER"), platform = "LOCAL")
            then("ForbiddenException 에러를 발생시킨다.") {
                val exception = shouldThrow<ForbiddenException> {
                    commentEntity.checkPermission(principal)
                }

                exception.message shouldBe "수정 권한이 없습니다."
            }
        }
    }
})