package dev.pimentel.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class UnitTest {

    @Test
    fun `should return two`() {
        val testClass = TestClass()

        assertEquals(2, testClass.returnTwo())
    }
}
