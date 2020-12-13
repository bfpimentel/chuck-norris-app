package dev.pimentel.chucknorris.presentation.facts.mappers

import dev.pimentel.chucknorris.presentation.facts.data.ShareableFact
import dev.pimentel.domain.entities.Fact
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ShareableFactMapperTest {

    private lateinit var mapper: ShareableFactMapper

    @BeforeEach
    @Test
    fun `should setup subject and it must not be null`() {
        mapper = ShareableFactMapperImpl()
        assertNotNull(mapper)
    }

    @Test
    fun `should map fact and return shareable fact`() {
        val fact = Fact("id1", "category1", "url1", "value1")
        val shareableFact = ShareableFact("url1", "value1")

        assertEquals(mapper.map(fact), shareableFact)
    }
}
