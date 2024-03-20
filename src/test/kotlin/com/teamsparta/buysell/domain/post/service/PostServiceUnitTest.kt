package com.teamsparta.buysell.domain.post.service

import com.teamsparta.buysell.domain.member.repository.MemberRepository
import com.teamsparta.buysell.domain.post.repository.PostRepository
import com.teamsparta.buysell.domain.post.repository.WishListRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.clearAllMocks
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith

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

    given("게시글을 작성하려고 할 때"){
        `when`("멤버가 존재하지 않을 경우"){
            then("ModelNotFoundException 이 발생한다."){

            }
        }
    }
})