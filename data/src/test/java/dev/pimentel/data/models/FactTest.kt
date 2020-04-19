package dev.pimentel.data.models

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class FactTest {

    @Test
    fun `Fact must not contain any null properties`() {
        val fact = Fact(listOf("category"), "url", "value")

        assertNotNull(fact.categories)
        assertNotNull(fact.url)
        assertNotNull(fact.value)
    }
}
