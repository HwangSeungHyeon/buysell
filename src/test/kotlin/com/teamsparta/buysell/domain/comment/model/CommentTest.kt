package com.teamsparta.buysell.domain.comment.model

import com.teamsparta.buysell.domain.comment.dto.request.CreateRequest
import com.teamsparta.buysell.domain.comment.dto.request.UpdateRequest
import com.teamsparta.buysell.domain.exception.ForbiddenException
import com.teamsparta.buysell.domain.member.model.Member
import com.teamsparta.buysell.domain.member.model.Platform
import com.teamsparta.buysell.domain.member.model.Role
import com.teamsparta.buysell.domain.post.model.Category
import com.teamsparta.buysell.domain.post.model.Post
import com.teamsparta.buysell.infra.security.UserPrincipal
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

class CommentTest {

    // 판매자 객체
    private val member1 = Member(
        email = "test@gmail.com",
        password = "!test123",
        nickname = "데네브",
        role = Role.MEMBER,
        gender = "남자",
        birthday = "2024-03-06",
        platform = Platform.LOCAL,
    )

    private val post = Post(
        title = "아이패드 프로 4세대 m2 11인치 128기가 스페이스그레이",
        content = "풀박스이고 25년 1월 4일까지 보증기간입니다\n" +
                "기스나 하자 없습니다\n" +
                "배터리 성능 100\n" +
                "인강용으로 사용했고 정말 깨끗하게 사용하였습니다\n" +
                "직거래는 삼송역 가능합니다\n" +
                "에눌 문의 정중히 사양합니다",
        view = 0,
        price = 950000,
        member = member1,
        category = Category.FOOD
    )

    // 구매자 객체
    private val member2 = Member(
        email = "test2@gmail.com",
        password = "!test1234",
        nickname = "알타이르",
        role = Role.MEMBER,
        gender = "남자",
        birthday = "2024-03-06",
        platform = Platform.LOCAL,
    )

    // 1. createRequest DTO가 Comment.makeEntity 메서드의 인자로 전달되었을 때 Comment 객체가 제대로 생성되는지 확인
    // 2. updateRequest DTO가 edit 메서드의 인자로 전달되었을 때 Comment 객체 내용이 제대로 수정되는지 확인
    // 3. 로그인한 사람과 댓글을 작성한 사람이 같은 사람일 경우
    // 4. 로그인한 사람과 댓글을 작성한 사람이 다른 사람일 경우

    @Test
    fun `createRequest DTO 가 전달되었을 때 Comment 객체가 제대로 생성되는지 확인`(){
        // GIVEN : 테스트에 필요한 사전정보 생성
        val tempText = "테스트용으로 사용할 댓글"
        val createRequest = CreateRequest(tempText)

        // WHEN : 테스트 할 메서드 호출
        val commentEntity = Comment.makeEntity(createRequest, member2, post)

        // THEN : 결과값 검증
        commentEntity.id shouldBe null
        commentEntity.member.nickname shouldBe "알타이르"
        commentEntity.content shouldBe tempText
        commentEntity.post.title shouldBe "아이패드 프로 4세대 m2 11인치 128기가 스페이스그레이"
    }


    @Test
    fun `updateRequest DTO가 edit 메서드의 인자로 전달되었을 때 Comment 객체 내용이 제대로 수정되는지 확인`(){
        // GIVEN : 테스트에 필요한 사전정보 생성
        val tempText = "테스트용으로 사용할 댓글"
        val createRequest = CreateRequest(tempText)
        val commentEntity = Comment.makeEntity(createRequest, member2, post)

        val editedText = "테스트용으로 사용할 댓글인데 수정도 했어"
        val updateRequest = UpdateRequest(editedText)

        // WHEN : 테스트 할 메서드 호출
        commentEntity.edit(updateRequest)

        // THEN : 결과값 검증
        commentEntity.content shouldBe editedText
    }

    @Test
    fun `로그인한 사람과 댓글을 작성한 사람이 같은 사람일 경우`(){
        // GIVEN : 테스트에 필요한 사전정보 생성
        val tempText = "테스트용으로 사용할 댓글"
        val createRequest = CreateRequest(tempText)
        val commentEntity = Comment.makeEntity(createRequest, member2, post)

        val userPrincipal = UserPrincipal(id = 1, email = "test2@gmail.com", role = setOf("MEMBER"), platform = "LOCAL")

        commentEntity.member.id = 1

        // WHEN & THEN: 테스트 할 메서드 호출하고 결과값 검증
        commentEntity.checkPermission(userPrincipal)
    }

    @Test
    fun `로그인한 사람과 댓글을 작성한 사람이 다른 사람일 경우 에러를 발생시키는지 확인`(){
        // GIVEN : 테스트에 필요한 사전정보 생성
        val tempText = "테스트용으로 사용할 댓글"
        val createRequest = CreateRequest(tempText)
        val commentEntity = Comment.makeEntity(createRequest, member2, post)

        commentEntity.member.id = 1 //DB에 들어가서 id가 1이라고 가정

        val userPrincipal = UserPrincipal(id = 100000, email = "test2@gmail.com", role = setOf("MEMBER"), platform = "LOCAL")

        // WHEN & THEN: 테스트 할 메서드 호출하고 결과값 검증
        val exception = shouldThrow<ForbiddenException> {
            commentEntity.checkPermission(userPrincipal)
        }

        exception.message shouldBe "수정 권한이 없습니다."
    }
}