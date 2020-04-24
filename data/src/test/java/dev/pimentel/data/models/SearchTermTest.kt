package dev.pimentel.data.models

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class SearchTermTest {

    @Test
    fun `SearchTerm must not contain any null properties`() {
        val searchTerm = SearchTerm(1, "query")

        assertNotNull(searchTerm.id)
        assertNotNull(searchTerm.term)
    }
}
