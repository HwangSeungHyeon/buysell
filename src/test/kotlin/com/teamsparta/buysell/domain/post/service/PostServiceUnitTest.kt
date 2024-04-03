package com.teamsparta.buysell.domain.post.service

import com.teamsparta.buysell.domain.exception.ForbiddenException
import com.teamsparta.buysell.domain.exception.ModelNotFoundException
import com.teamsparta.buysell.domain.member.model.Account
import com.teamsparta.buysell.domain.member.model.Member
import com.teamsparta.buysell.domain.member.model.Platform
import com.teamsparta.buysell.domain.member.model.Role
import com.teamsparta.buysell.domain.member.repository.MemberRepository
import com.teamsparta.buysell.domain.post.dto.request.CreatePostRequest
import com.teamsparta.buysell.domain.post.dto.request.UpdatePostRequest
import com.teamsparta.buysell.domain.post.model.Category
import com.teamsparta.buysell.domain.post.model.Post
import com.teamsparta.buysell.domain.post.repository.PostRepository
import com.teamsparta.buysell.domain.post.repository.WishListRepository
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
class PostServiceUnitTest: BehaviorSpec({
    extension(SpringExtension)

    afterContainer {
        clearAllMocks()
    }

    // 종속 repository mocking
    val memberRepository = mockk<MemberRepository>()
    val postRepository = mockk<PostRepository>()
    val wishListRepository = mockk<WishListRepository>()

    val postService = PostServiceImpl(postRepository, memberRepository, wishListRepository)


    val createRequest = CreatePostRequest(
        title = "소파 팝니다",
        content = "급처로 팜",
        price = 10000,
        category = Category.INTERIOR,
        imageUrl = ""
    )

    val updateRequest = UpdatePostRequest(
        title = "의자 팝니다",
        content = "네고 없습니다",
        price = 100000,
        category = Category.INTERIOR,
        imageUrl = ""
    )

    val member = Member(
        email = "test@gmail.com",
        password = "!test123",
        nickname = "데네브",
        role = Role.MEMBER,
        gender = "남자",
        birthday = "2024-03-06",
        platform = Platform.LOCAL,
        account = Account()
    ).apply { id=1 }

    val post = Post(
        title = createRequest.title,
        content = createRequest.content,
        view = 0,
        price = createRequest.price,
        member = member,
        category = createRequest.category,
        imageUrl = ""
    )

    given("게시글 작성 요청이 왔을 때"){
        val principal = UserPrincipal(id = 1, email = "test@gmail.com", role = setOf("MEMBER"), platform = "LOCAL")

        `when`("멤버가 DB에 존재하지 않을 경우"){
            every { memberRepository.findByIdOrNull(any()) } returns null
            then("ModelNotFound 에러가 발생해야 한다."){
                shouldThrow<ModelNotFoundException> {
                    postService.createPost(createRequest, principal)
                }
            }
        }
        
        `when`("멤버가 DB에 존재할 경우"){
            every { memberRepository.findByIdOrNull(any()) } returns member
            then("게시글이 작성되어야 한다."){

                every { postRepository.save(any()) } returns post
                val result = postService.createPost(createRequest, principal)
                
                result.message shouldBe "상품을 등록하였습니다."
            }
        }
    }

    given("게시글 수정 요청이 왔을 때"){
        val postId = 1
        val principal = UserPrincipal(id = 1, email = "test@gmail.com", role = setOf("MEMBER"), platform = "LOCAL")
        `when`("작성된 게시글이 없다면"){
            every { postRepository.findByIdOrNull(any()) } returns null

            then("ModelNotFound 에러가 발생해야 한다."){
                shouldThrow<ModelNotFoundException> {
                    postService.updatePost(postId, updateRequest, principal)
                }
            }
        }
        `when`("게시글은 있지만 게시글 수정 권한이 없다면"){
            every { postRepository.findByIdOrNull(any()) } returns post
            val anotherPrinciple = UserPrincipal(id = 2, email = "test@gmail.com", role = setOf("MEMBER"), platform = "LOCAL")

            then("ForbiddenException 에러가 발생해야 한다."){
                shouldThrow<ForbiddenException> {
                    postService.updatePost(postId, updateRequest, anotherPrinciple)
                }
            }
        }
        `when`("게시글 수정 권한이 있다면"){
            every { postRepository.findByIdOrNull(any()) } returns post
            then("게시글이 수정되어야 한다."){
                val result = postService.updatePost(postId, updateRequest, principal)

                result.message shouldBe "상품 정보가 수정되었습니다."
            }
        }
    }

    given("게시글 삭제 요청이 왔을 때"){
        val postId = 1
        val principal = UserPrincipal(id = 1, email = "test@gmail.com", role = setOf("MEMBER"), platform = "LOCAL")
        `when`("작성된 게시글이 없다면"){
            every { postRepository.findByIdOrNull(any()) } returns null

            then("ModelNotFound 에러가 발생해야 한다."){
                shouldThrow<ModelNotFoundException> {
                    postService.deletePost(postId, principal)
                }
            }
        }
        `when`("게시글은 있지만 게시글 삭제 권한이 없다면"){
            every { postRepository.findByIdOrNull(any()) } returns post
            val anotherPrinciple = UserPrincipal(id = 2, email = "test@gmail.com", role = setOf("MEMBER"), platform = "LOCAL")

            then("ForbiddenException 에러가 발생해야 한다."){
                shouldThrow<ForbiddenException> {
                    postService.deletePost(postId, anotherPrinciple)
                }
            }
        }
        `when`("게시글 삭제 권한이 있다면"){
            every { postRepository.findByIdOrNull(any()) } returns post
            every { postRepository.delete(any()) } returns Unit
            then("게시글이 삭제되어야 한다."){
                val result = postService.deletePost(postId, principal)

                result.message shouldBe "상품이 삭제되었습니다."
            }
        }
    }

    given("게시글 목록 조회에 Pagination을 적용했을 때"){
        val pageNumber=0
        val pageSize=10
        val sort = "title"

        `when`("카테고리를 선택하지 않았을 경우"){


            then("삭제된 게시글이 없다면 모든 게시글을 가져와야 한다."){

            }
        }

        `when`("카테고리를 선택했을 경우"){
            then("삭제된 게시글이 있다면 해당 게시글은 제외되어야 한다."){

            }

            then("삭제된 게시글이 없다면 모든 게시글을 가져와야 한다."){

            }
        }

        `when`("검색 키워드를 입력했을 경우"){
            then("삭제된 게시글이 있다면 해당 게시글은 제외되어야 한다."){

            }

            then("삭제된 게시글이 없다면 모든 게시글을 가져와야 한다."){

            }
        }

        `when`("검색 키워드를 입력하지 않았을 경우"){
            then("삭제된 게시글이 있다면 해당 게시글은 제외되어야 한다."){

            }

            then("삭제된 게시글이 없다면 모든 게시글을 가져와야 한다."){

            }
        }
    }
})