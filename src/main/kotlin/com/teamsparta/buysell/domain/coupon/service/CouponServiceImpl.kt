package com.teamsparta.buysell.domain.coupon.service

import com.teamsparta.buysell.domain.common.dto.MessageResponse
import com.teamsparta.buysell.domain.coupon.dto.request.CreateCouponRequest
import com.teamsparta.buysell.domain.coupon.moedel.Coupon
import com.teamsparta.buysell.domain.coupon.repository.AppliedUserRepository
import com.teamsparta.buysell.domain.coupon.repository.CouponRepository
import com.teamsparta.buysell.domain.coupon.repository.RedisCouponRepository
import com.teamsparta.buysell.domain.exception.ModelNotFoundException
import com.teamsparta.buysell.domain.member.repository.MemberRepository
import com.teamsparta.buysell.infra.utility.CouponUtility
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class CouponServiceImpl(
    private val couponRepository: CouponRepository,
    private val memberRepository: MemberRepository,
    private val couponUtility: CouponUtility,
    private val appliedUserRepository: AppliedUserRepository,
    private val redisCouponRepository: RedisCouponRepository
) : CouponService {
    @Transactional
    override fun createCoupon(request: CreateCouponRequest, memberId: Int): MessageResponse {
        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw ModelNotFoundException("member", memberId)
        val apply = appliedUserRepository.add(memberId)

        if (apply != 1L) {
            return MessageResponse("계정당 1회만 참여 가능합니다.")
        }
        val count = redisCouponRepository.increment()

        if (count > request.couponCount) {
            return MessageResponse("준비된 쿠폰이 모두 소진되었습니다.")
        }

        val couponExp = LocalDateTime.now().plus(request.couponExp, ChronoUnit.DAYS)

        val couponNumber = couponUtility.createCouponNumber()
        val coupon = Coupon(
            content = request.content,
            couponNumber = couponNumber,
            couponCount = request.couponCount,
            memberId = member,
            available = true,
            couponExp = couponExp
        )
        couponRepository.save(coupon)

        return MessageResponse("쿠폰이 발급되었습니다. $couponNumber")
    }
}
