package dev.pimentel.chucknorris.presentation.facts.mappers

import android.content.Context
import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.presentation.facts.data.FactViewData
import dev.pimentel.domain.entities.Fact
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FactViewDataMapperTest {

    private val context = mockk<Context>(relaxed = true)
    private lateinit var mapper: FactViewDataMapper

    @BeforeEach
    fun `should setup subject and it must not be null`() {
        mapper = FactViewDataMapperImpl(context)
    }

    @Test
    fun `should map facts and return fact displays`() {
        val uncategorized = "Uncategorized"

        val facts = listOf(
            Fact(
                id = "id1",
                category = "category1",
                url = "url1",
                value = "value1"
            ),
            Fact(
                id = "id2",
                category = null,
                url = "url2",
                value = "value2value2value2value2value2value2value2value2value2value2value2value2value2value2"
            ),
        )

        val factsDisplays = listOf(
            FactViewData(
                id = "id1",
                category = "Category1",
                value = "value1",
                fontSize = R.dimen.text_large
            ),
            FactViewData(
                id = "id2",
                category = uncategorized,
                value = "value2value2value2value2value2value2value2value2value2value2value2value2value2value2",
                fontSize = R.dimen.text_normal
            )
        )

        every { context.getString(R.string.get_facts_no_category) } returns uncategorized

        assertEquals(mapper.map(facts), factsDisplays)

        verify(exactly = 1) { context.getString(R.string.get_facts_no_category) }
        confirmVerified(context)
    }
}
