package com.teamsparta.buysell.domain.member.service

import com.teamsparta.buysell.domain.common.dto.MessageResponse
import com.teamsparta.buysell.domain.member.dto.request.SignUpRequest
import com.teamsparta.buysell.domain.member.model.Account
import com.teamsparta.buysell.domain.member.model.Member
import com.teamsparta.buysell.domain.member.model.Platform
import com.teamsparta.buysell.domain.member.model.Role
import com.teamsparta.buysell.domain.member.repository.MemberRepository
import com.teamsparta.buysell.infra.security.jwt.JwtPlugin
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@ExtendWith(MockKExtension::class)
class MemberServiceUnitTest: BehaviorSpec({
    extension(SpringExtension)

    afterContainer {
        clearAllMocks()
    }

    val memberRepository = mockk<MemberRepository>()

    val passwordEncoder = BCryptPasswordEncoder()

    val jwtPlugin = JwtPlugin(
        issuer = "test",
        secret = "testtesttesttesttesttesttesttesttesttest",
        accessTokenExpirationHour = 1,
        refreshTokenExpirationHour = 1
    )
    val authLinkService = mockk<AuthLinkService>()

    val memberService = MemberServiceImpl(memberRepository, passwordEncoder, jwtPlugin, authLinkService)

    val duplicatedName = "다프네"
    val notDuplicatedName = "리제"

    val signedMember = Member(
        email = "test@gmail.com",
        password = "test123!",
        nickname = duplicatedName,
        gender = "남자",
        birthday = "2024-03-20",
        account = Account(),
        platform = Platform.LOCAL,
        role = Role.MEMBER
    )

    given("회원 가입 요청이 왔을 때"){
        val request = SignUpRequest(
            email = "daphne@gmail.com",
            password = "daphne123!",
            nickname = duplicatedName,
            gender = "여자",
            birthday = "2038-12-31"
        )

        val member = Member(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            nickname = request.nickname,
            role = Role.MEMBER,
            gender = request.gender,
            birthday = request.birthday,
            platform = Platform.LOCAL,
            account = Account(),
            isVerified = false
        )

        `when`("DB에 중복 닉네임이 있다면"){
            then("DataIntegrityViolationException 에러를 발생시킨다."){
                every { memberRepository.existsByNickname(any()) } returns true
                every { memberRepository.existsByEmailAndPlatform(any(), any()) } returns false
                every { authLinkService.sendAuthEmail(any()) } returns MessageResponse("이메일로 인증코드를 발송하였습니다.")

                shouldThrow<DataIntegrityViolationException>{
                    memberService.signUp(request)
                }
            }
        }

        `when`("이미 가입된 이메일이 있다면"){
            then("DataIntegrityViolationException 에러를 발생시킨다."){
                every { memberRepository.existsByNickname(any()) } returns false
                every { memberRepository.existsByEmailAndPlatform(any(), any()) } returns true
                every { authLinkService.sendAuthEmail(any()) } returns MessageResponse("이메일로 인증코드를 발송하였습니다.")

                shouldThrow<DataIntegrityViolationException>{
                    memberService.signUp(request)
                }
            }
        }
    }

    // 로그인 하려고 할 때
    // 회원 탈퇴 하려고 할 때
})