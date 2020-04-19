package dev.pimentel.data.models

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class FactsResponseTest {

    @Test
    fun `FactsResponse must not contain any null properties`() {
        val response = FactsResponse(listOf(FactsResponse.Fact(listOf("category"), "url", "value")))

        assertNotNull(response.result)

        val fact = response.result.first()

        assertNotNull(fact.categories)
        assertNotNull(fact.url)
        assertNotNull(fact.value)
    }
}
