package com.teamsparta.buysell

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

//class 지정해야 에러가 발생 X
@SpringBootTest(classes = [BuysellApplicationTests::class])
class BuysellApplicationTests {

    @Test
    fun contextLoads() {
    }

}
