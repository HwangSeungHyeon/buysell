package com.teamsparta.buysell.domain.order.service

import com.teamsparta.buysell.domain.common.dto.MessageResponse
import com.teamsparta.buysell.domain.exception.ModelNotFoundException
import com.teamsparta.buysell.domain.member.repository.MemberRepository
import com.teamsparta.buysell.domain.order.dto.request.CreateOrderRequest
import com.teamsparta.buysell.domain.order.model.Order
import com.teamsparta.buysell.domain.order.repository.OrderRepository
import com.teamsparta.buysell.domain.post.repository.PostRepository
import com.teamsparta.buysell.infra.security.UserPrincipal
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class OrderServiceImpl(
    private val memberRepository: MemberRepository,
    private val postRepository: PostRepository,
    private val orderRepository: OrderRepository
) : OrderService {
    @Transactional
    override fun createOrder(
        postId: Int,
        request: CreateOrderRequest,
        principal: UserPrincipal
    ): MessageResponse {
        val member = memberRepository.findByIdOrNull(principal.id)
            ?: throw ModelNotFoundException("Member", principal.id)
        val post = postRepository.findByIdOrNull(postId)
            ?: throw ModelNotFoundException("Post", postId)

        val order = Order(
            address = request.address,
            phoneNumber = request.phoneNumber,
            member = member,
            post = post
        )

        member.account.payment(post.price)

        orderRepository.save(order)

            return MessageResponse("결제가 완료되었습니다.")
        }
    }