package com.teamsparta.buysell.domain.order.service

import com.teamsparta.buysell.domain.common.dto.MessageResponse
import com.teamsparta.buysell.domain.exception.ModelNotFoundException
import com.teamsparta.buysell.domain.member.repository.MemberRepository
import com.teamsparta.buysell.domain.order.dto.request.CreateOrderRequest
import com.teamsparta.buysell.domain.order.model.Order
import com.teamsparta.buysell.domain.order.model.OrderState
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

        post.myPostCheckPermission(principal)
        post.outOfStockStatus()

        val order = Order(
            address = request.address,
            phoneNumber = request.phoneNumber,
            member = member,
            post = post,
            orderState = OrderState.COMPLETED
        )

        member.account.payment(post.price)
        post.isSoldOut = true

        orderRepository.save(order)

        return MessageResponse("결제가 완료되었습니다.")
    }

    @Transactional
    override fun cancelOrder(
        postId: Int,
        orderId: Int,
        principal: UserPrincipal
    ): MessageResponse {
        val order = orderRepository.findByIdOrNull(orderId)
            ?: throw ModelNotFoundException("Order", orderId)

        if (order.orderState != OrderState.COMPLETED) {
            throw IllegalStateException("이미 취소된 주문입니다.")
        }

        val member = order.member
        val refundAmount = order.post.price

        member.account.refundToAccount(refundAmount)

        order.orderState = OrderState.CANCELLED

        return MessageResponse("결제가 취소되었습니다.")
    }
}