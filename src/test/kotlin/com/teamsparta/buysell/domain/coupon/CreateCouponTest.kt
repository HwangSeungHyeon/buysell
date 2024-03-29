//package com.teamsparta.buysell.domain.coupon
//
//import com.teamsparta.buysell.domain.coupon.dto.request.CreateCouponRequest
//import com.teamsparta.buysell.domain.coupon.repository.AppliedUserRepository
//import com.teamsparta.buysell.domain.coupon.repository.CouponRepository
//import com.teamsparta.buysell.domain.coupon.repository.RedisCouponRepository
//import com.teamsparta.buysell.domain.coupon.service.CouponService
//import com.teamsparta.buysell.domain.member.repository.MemberRepository
//import org.assertj.core.api.Assertions.assertThat
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.test.context.TestPropertySource
//import java.util.concurrent.CountDownLatch
//import java.util.concurrent.ExecutorService
//import java.util.concurrent.Executors
//
//@SpringBootTest
//class CreateCouponTest {
//    @Autowired
//    private lateinit var couponRepository: CouponRepository
//
//    @Autowired
//    private lateinit var couponService: CouponService
//
//    @Autowired
//    private lateinit var redisCouponRepository: RedisCouponRepository
//
//    @Autowired
//    private lateinit var memberRepository: MemberRepository
//
//    @Autowired
//    private lateinit var appliedUserRepository: AppliedUserRepository
//
//
//    @Test
//    fun 한명당_하나의쿠폰만_발급받을수_있음() {
//        val threadCount = 1000
//        val executorService: ExecutorService = Executors.newFixedThreadPool(32)
//        val latch = CountDownLatch(threadCount)
//
//        repeat(threadCount) { i ->
//            val userId = i.toLong()
//            executorService.submit {
//                couponService.createCoupon(CreateCouponRequest(50,"dd",5), 1)
//                latch.countDown()
//            }
//        }
//        latch.await()
//        val count = couponRepository.count()
//
//        assertThat(count).isEqualTo(1)
//    }
//
//    @Test
//    fun 응모를_여러번_시도() {
//        val threadCount = 1000
//        val executorService: ExecutorService = Executors.newFixedThreadPool(32)
//        val latch = CountDownLatch(threadCount)
//
//        repeat(threadCount) { i ->
//            val userId = i.toLong()
//            executorService.submit {
//                couponService.createCoupon(CreateCouponRequest(20,"dd",5), 1)
//                latch.countDown()
//            }
//        }
//        latch.await()
//        val count = couponRepository.count()
//
//        assertThat(count).isEqualTo(20)
//    }
//}