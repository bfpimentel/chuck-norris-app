package dev.pimentel.domain.usecases

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ShuffleListTest {

    private lateinit var shuffleList: ShuffleList

    @BeforeEach
    @Test
    fun `should setup subject and it must not be null`() {
        shuffleList = ShuffleList()

        assertNotNull(shuffleList)
    }

    @Test
    fun `should return a list of the same size as the one that has been inputted`() {
        val list = listOf(
            "item1",
            "item2"
        )

        assertEquals(
            list.size,
            shuffleList(ShuffleList.Params(list)).size
        )
    }

    @Test
    fun `Params must contain a not null list`() {
        val list = listOf(
            "item1",
            "item2"
        )

        val params = ShuffleList.Params(list)

        assertNotNull(params.list)
    }
}
