package com.teamsparta.buysell.domain.post.service

import com.teamsparta.buysell.domain.common.dto.MessageResponse
import com.teamsparta.buysell.domain.exception.ModelNotFoundException
import com.teamsparta.buysell.domain.member.model.Member
import com.teamsparta.buysell.domain.member.repository.MemberRepository
import com.teamsparta.buysell.domain.post.dto.request.CreatePostRequest
import com.teamsparta.buysell.domain.post.dto.request.UpdatePostRequest
import com.teamsparta.buysell.domain.post.dto.response.PostListResponse
import com.teamsparta.buysell.domain.post.dto.response.PostResponse
import com.teamsparta.buysell.domain.post.dto.response.WishResponse
import com.teamsparta.buysell.domain.post.model.Category
import com.teamsparta.buysell.domain.post.model.Post
import com.teamsparta.buysell.domain.post.model.WishList
import com.teamsparta.buysell.domain.post.repository.PostRepository
import com.teamsparta.buysell.domain.post.repository.WishListRepository
import com.teamsparta.buysell.infra.security.UserPrincipal
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PostServiceImpl(
    private val postRepository: PostRepository,
    private val memberRepository: MemberRepository,
    private val wishListRepository: WishListRepository
) : PostService {
    @Transactional
    override fun createPost(request: CreatePostRequest, principal: UserPrincipal): MessageResponse {
        val member = getMember(principal)

        postRepository.save(
            Post(
                title = request.title,
                content = request.content,
                view = 0,
                price = request.price,
                member = member,
                category = request.category,
                imageUrl = request.imageUrl
            )
        )

        return MessageResponse("상품을 등록하였습니다.")
    }

    @Transactional
    override fun updatePost(
        postId: Int,
        request: UpdatePostRequest,
        principal: UserPrincipal
    ): MessageResponse {
        val post = postRepository.findByIdOrNull(postId)
            ?: throw ModelNotFoundException("post", postId)

        post.checkPermission(principal)
        post.postUpdate(request)

        return MessageResponse("상품 정보가 수정되었습니다.")
    }

    override fun getPosts(): List<PostResponse> {
        return postRepository.findAll().map { it.toResponse() }
    }

    @Transactional
    override fun getPostById(postId: Int): PostResponse {
        val post = postRepository.findByIdOrNull(postId)
            ?: throw ModelNotFoundException("post", postId)
        post.apply { increaseView() }
        return post.toResponse()
    }

    override fun deletePost(postId: Int, principal: UserPrincipal): MessageResponse {
        val post = postRepository.findByIdOrNull(postId)
            ?: throw ModelNotFoundException("post", postId)

        post.checkPermission(principal)

        postRepository.delete(post)
        return MessageResponse("상품이 삭제되었습니다.")
    }

    //게시글을 조회할 때 Pagination을 적용한 메서드
    //카테고리가 없을 경우 기존과 동일하게 동작
    //카테고리가 있을 경우 해당 카테고리 관련 게시글만 조회
    override fun getPostsWithPagination(
        category: Category?,
        pageable: Pageable
    ): Page<PostListResponse> {
        return postRepository
            .getPostsWithPagination(category, pageable)
    }

    //키워드 검색 메서드
    override fun searchByKeyword(
        keyword: String,
        pageable: Pageable
    ): Page<PostListResponse> {
        return postRepository
            .searchByKeyword(keyword, pageable)
    }

    override fun getMyWishByPostId(
        postId: Int,
        userPrincipal: UserPrincipal
    ): WishResponse {
        val myWish = wishListRepository.findByPostIdAndMemberId(postId, userPrincipal.id, WishResponse::class.java)
            ?: WishResponse(-9)

        return myWish
    }

    override fun addWishList(
        postId: Int,
        userPrincipal: UserPrincipal
    ) : MessageResponse {
        val post = getPost(postId)

        //작성자는 자기 게시글에 찜 버튼을 누를 수 없다
        WishList.checkPermission(post, userPrincipal)

        wishListRepository.existsByPostIdAndMemberId(postId,userPrincipal.id)
            .let { if(it) throw IllegalStateException("이미 위시리스트에 등록하셨습니다.") }

        val member = getMember(userPrincipal)

        WishList.makeEntity(post = post, member = member)
            .let { wishListRepository.save(it) }

        return MessageResponse("위시리스트에 등록하였습니다.")
    }

    @Transactional
    override fun cancelWishList(
        postId: Int,
        userPrincipal: UserPrincipal
    ): MessageResponse {
        val post = postRepository.findByIdOrNull(postId)
            ?: throw ModelNotFoundException("post", postId)

        //작성자는 자기 게시글에 찜 버튼을 누를 수 없다
        WishList.checkPermission(post, userPrincipal)

        if (wishListRepository.existsByPostIdAndMemberId(postId,userPrincipal.id)) {
            wishListRepository.deleteByPostIdAndMemberId(postId, userPrincipal.id)
            return MessageResponse("위시리스트에서 삭제하였습니다.")
        }
        else{
            return MessageResponse("잘못된 동작입니다.")
        }
    }

    private fun getPost(postId: Int): Post{
        return postRepository.findByIdOrNull(postId)
            ?: throw ModelNotFoundException("post", postId)
    }

    private fun getMember(userPrincipal: UserPrincipal): Member{
        return memberRepository.findByIdOrNull(userPrincipal.id)
            ?: throw ModelNotFoundException("model", userPrincipal.id)
    }
}