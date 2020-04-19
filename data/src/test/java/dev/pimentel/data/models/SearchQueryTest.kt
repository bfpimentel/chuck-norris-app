package dev.pimentel.data.models

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class SearchQueryTest {

    @Test
    fun `SearchQuery must not contain any null properties`() {
        val searchQuery = SearchQuery(1, "query")

        assertNotNull(searchQuery.query)
    }
}
