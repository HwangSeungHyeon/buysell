package com.teamsparta.buysell.domain.post.service

import com.teamsparta.buysell.domain.exception.ModelNotFoundException
import com.teamsparta.buysell.domain.member.model.Member
import com.teamsparta.buysell.domain.member.repository.MemberRepository
import com.teamsparta.buysell.domain.post.dto.request.CreatePostRequest
import com.teamsparta.buysell.domain.post.dto.request.UpdatePostRequest
import com.teamsparta.buysell.domain.post.dto.response.PostListResponse
import com.teamsparta.buysell.domain.post.dto.response.PostResponse
import com.teamsparta.buysell.domain.post.model.Post
import com.teamsparta.buysell.domain.post.model.toResponse
import com.teamsparta.buysell.domain.post.repository.PostRepository
import com.teamsparta.buysell.infra.security.UserPrincipal
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PostServiceImpl(
    private val postRepository: PostRepository,
    private val memberRepository: MemberRepository
) : PostService {
    @Transactional
    override fun createPost(request: CreatePostRequest, principal: UserPrincipal): PostResponse {
        val member = getMember(principal)

        return postRepository.save(
            Post(
                title = request.title,
                content = request.content,
                createdName = member.nickname,
                view = 0,
                price = request.price,
                member = member
//                category = request.category
            )
        ).toResponse()
    }

    @Transactional
    override fun updatePost(
        postId: Int,
        request: UpdatePostRequest,
        principal: UserPrincipal
    ): PostResponse {
        val post = postRepository.findByIdOrNull(postId)
            ?: throw ModelNotFoundException("post", postId)

        post.checkPermission(principal)

        post.title = request.title
        post.content = request.content
//        post.category = request.category

        return postRepository.save(post).toResponse()
    }

    override fun getPosts(): List<PostResponse> {
        return postRepository.findAll().map { it.toResponse() }

    }

    override fun getPostById(postId: Int): PostResponse {
        val post = postRepository.findByIdOrNull(postId)
            ?: throw ModelNotFoundException("post", postId)
        return post.toResponse()
    }

    @Transactional
    override fun deletePost(postId: Int, principal: UserPrincipal) {
        val post = postRepository.findByIdOrNull(postId)
            ?: throw ModelNotFoundException("post", postId)

        post.checkPermission(principal)

//        postRepository.delete(post)
        post.softDelete()
    }

    private fun getMember(principal: UserPrincipal): Member {
        return memberRepository.findByIdOrNull(principal.id)
            ?: throw ModelNotFoundException("Model", principal.id)
    }

    override fun searchByKeyword(
        keyword: String,
        pageable: Pageable
    ): Page<PostListResponse> {
        return postRepository
            .searchByKeyword(keyword, pageable)
    }
}