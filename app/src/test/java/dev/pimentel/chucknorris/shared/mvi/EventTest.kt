package dev.pimentel.chucknorris.shared.mvi

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull

class EventTest {

    @Test
    fun `event value must not be null when first accessing it`() {
        val event = "".toEvent()

        assertNotNull(event.value)
    }

    @Test
    fun `event value must be null when accessing it for the second time`() {
        val event = "".toEvent()

        assertNotNull(event.value)
        assertNull(event.value)
    }

    @Test
    fun `handle event must execute block when first calling it`() {
        val event = "".toEvent()

        var expectedResult: String? = null

        event.handleEvent { expectedResult = it }

        assertNotNull(expectedResult)
    }

    @Test
    fun `handle event must not execute block when calling it for the second time`() {
        val event = "".toEvent()

        var expectedResult: String? = null

        event.handleEvent { expectedResult = it }
        event.handleEvent { expectedResult = null }

        assertNotNull(expectedResult)
    }
}
