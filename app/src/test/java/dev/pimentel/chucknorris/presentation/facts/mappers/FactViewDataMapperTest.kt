package dev.pimentel.chucknorris.presentation.facts.mappers

import android.content.Context
import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.presentation.facts.data.FactViewData
import dev.pimentel.domain.entities.Fact
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FactViewDataMapperTest {

    private val context = mockk<Context>(relaxed = true)
    private lateinit var mapper: FactViewDataMapper

    @BeforeEach
    @Test
    fun `should setup subject and it must not be null`() {
        mapper = FactViewDataMapperImpl(context)
        assertNotNull(mapper)
    }

    @Test
    fun `should map facts and return fact displays`() {
        val uncategorized = "Uncategorized"

        every { context.getString(R.string.get_facts_no_category) } returns uncategorized

        val facts = listOf(
            Fact("id1", "category1", "url1", "value1"),
            Fact("id2", null, "url2", "value2")
        )

        val factsDisplays = listOf(
            FactViewData("id1", "Category1", "value1", R.dimen.text_large),
            FactViewData("id2", uncategorized, "value2", R.dimen.text_large)
        )

        assertEquals(mapper.map(facts), factsDisplays)
    }
}
