//package com.teamsparta.buysell.domain.comment.service
//
//import com.teamsparta.buysell.domain.comment.dto.request.CreateRequest
//import com.teamsparta.buysell.domain.comment.dto.request.UpdateRequest
//import com.teamsparta.buysell.domain.comment.repository.CommentRepository
//import com.teamsparta.buysell.domain.common.BaseRepositoryTest
//import com.teamsparta.buysell.domain.exception.ForbiddenException
//import com.teamsparta.buysell.domain.member.model.Member
//import com.teamsparta.buysell.domain.member.model.Platform
//import com.teamsparta.buysell.domain.member.model.Role
//import com.teamsparta.buysell.domain.member.repository.MemberRepository
//import com.teamsparta.buysell.domain.post.model.Category
//import com.teamsparta.buysell.domain.post.model.Post
//import com.teamsparta.buysell.domain.post.repository.PostRepository
//import com.teamsparta.buysell.infra.security.UserPrincipal
//import io.kotest.assertions.throwables.shouldThrow
//import io.kotest.matchers.shouldBe
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//
//
//class CommentServiceTest @Autowired constructor(
//    private val commentRepository: CommentRepository,
//    private val postRepository: PostRepository,
//    private val memberRepository : MemberRepository
//): BaseRepositoryTest() {
//
//    private val commentService = CommentServiceImpl(commentRepository, postRepository, memberRepository)
//
//    // 1. addComment - 로그인한 멤버가 특정 게시글에 답변을 달 수 있는지 확인
//    // 2. editComment - 로그인한 멤버가 자신이 단 댓글을 수정할 수 있는지 확인
//    // 3. editComment - 로그인한 멤버가 다른 사람의 댓글을 수정할 수 있는지 확인
//    // 4. deleteComment - 로그인한 멤버가 자신이 단 댓글을 삭제할 수 있는지 확인
//    // 5. deleteComment - 로그인한 멤버가 다른 사람의 댓글을 삭제할 수 있는지 확인
//
//    private val postWriter = Member(
//        email = "test@gmail.com",
//        password = "!test123",
//        nickname = "데네브",
//        role = Role.MEMBER,
//        gender = "남자",
//        birthday = "2024-03-06",
//        platform = Platform.LOCAL,
//    )
//
//    private val consumer = Member(
//        email = "test2@gmail.com",
//        password = "!test123",
//        nickname = "데네브",
//        role = Role.MEMBER,
//        gender = "남자",
//        birthday = "2024-03-06",
//        platform = Platform.LOCAL,
//    )
//
//    private val post = Post(
//        title = "아이패드 프로 4세대 m2 11인치 128기가 스페이스그레이",
//        content = "풀박스이고 25년 1월 4일까지 보증기간입니다\n" +
//                "기스나 하자 없습니다\n" +
//                "배터리 성능 100\n" +
//                "인강용으로 사용했고 정말 깨끗하게 사용하였습니다\n" +
//                "직거래는 삼송역 가능합니다\n" +
//                "에눌 문의 정중히 사양합니다",
//        view = 0,
//        price = 950000,
//        member = postWriter,
//        category = Category.FOOD
//    )
//
//    private fun testInit(){
//        memberRepository.save(postWriter) //멤버 1이 가입
//        postRepository.save(post) //멤버 1이 게시글 작성
//        memberRepository.save(consumer) //멤버 2 가입
//    }
//
//    private fun makeCreateRequest(): CreateRequest{
//        return CreateRequest("테스트용으로 사용할 댓글")
//    }
//
//    private fun makeUpdateRequest(): UpdateRequest{
//        return UpdateRequest("테스트용으로 사용할 댓글인데 수정도 했어")
//    }
//
//    private val userPrincipal = UserPrincipal(id = 2, email = "test2@gmail.com", role = setOf("MEMBER"), platform = "LOCAL")
//    private val userPrincipal2 = UserPrincipal(id = 1, email = "test1@gmail.com", role = setOf("MEMBER"), platform = "LOCAL")
//
//    @Test
//    fun `addComment - 로그인한 멤버가 특정 게시글에 답변을 달 수 있는지 확인`(){
//        // GIVEN : 테스트에 필요한 사전정보 생성
//
//        val createRequest = makeCreateRequest()
//
//        testInit()
//
//        // WHEN : 테스트 할 메서드 호출
//        val result = commentService.addComment(postId = 1, request = createRequest, principal = userPrincipal)
//
//        // THEN : 결과값 검증
//        result.message shouldBe "댓글이 작성되었습니다."
//    }
//
//    @Test
//    fun `editComment - 로그인한 멤버가 자신이 단 댓글을 수정할 수 있는지 확인`(){
//        // GIVEN : 테스트에 필요한 사전정보 생성
//        testInit()
//
//        val createRequest = makeCreateRequest()
//        commentService.addComment(postId = 1, request = createRequest, principal = userPrincipal)
//        val updateRequest = makeUpdateRequest()
//
//        // WHEN : 테스트 할 메서드 호출
//        val result = commentService.editComment(postId = 1, commentId = 1, request = updateRequest, principal = userPrincipal)
//
//        // THEN : 결과값 검증
//        result.message shouldBe "댓글이 수정되었습니다."
//    }
//
//    @Test
//    fun `editComment - 로그인한 멤버가 다른 사람의 댓글을 수정할 수 있는지 확인`(){
//        // GIVEN : 테스트에 필요한 사전정보 생성
//        testInit()
//        val createRequest = makeCreateRequest()
//        commentService.addComment(postId = 1, request = createRequest, principal = userPrincipal)
//        val updateRequest = makeUpdateRequest()
//
//        // WHEN : 테스트 할 메서드 호출
//
//        val exception = shouldThrow<ForbiddenException> {
//            commentService.editComment(postId = 1, commentId = 1, request = updateRequest, principal = userPrincipal2)
//        }
//
//        // THEN : 결과값 검증
//        exception.message shouldBe "권한이 없습니다."
//    }
//
//    @Test
//    fun `deleteComment - 로그인한 멤버가 자신이 단 댓글을 삭제할 수 있는지 확인`(){
//        // GIVEN : 테스트에 필요한 사전정보 생성
//        testInit()
//        val createRequest = makeCreateRequest()
//        commentService.addComment(postId = 1, request = createRequest, principal = userPrincipal)
//
//        // WHEN : 테스트 할 메서드 호출
//        val result = commentService.deleteComment(postId = 1, commentId = 1, principal = userPrincipal)
//
//        // THEN : 결과값 검증
//        result.message shouldBe "댓글이 삭제되었습니다."
//    }
//
//    @Test
//    fun `deleteComment - 로그인한 멤버가 다른 사람의 댓글을 삭제할 수 있는지 확인`(){
//        // GIVEN : 테스트에 필요한 사전정보 생성
//        testInit()
//        val createRequest = makeCreateRequest()
//        commentService.addComment(postId = 1, request = createRequest, principal = userPrincipal)
//
//        // WHEN : 테스트 할 메서드 호출
//        val exception = shouldThrow<ForbiddenException> {
//            commentService.deleteComment(postId = 1, commentId = 1, principal = userPrincipal2)
//        }
//
//        // THEN : 결과값 검증
//        exception.message shouldBe "권한이 없습니다."
//    }
//}