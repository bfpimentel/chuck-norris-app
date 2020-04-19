package dev.pimentel.data.models

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class SearchTermTest {

    @Test
    fun `SearchQuery must not contain any null properties`() {
        val searchQuery = SearchTerm(1, "query")

        assertNotNull(searchQuery.term)
    }
}
