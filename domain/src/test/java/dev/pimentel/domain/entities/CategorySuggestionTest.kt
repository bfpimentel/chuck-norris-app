package dev.pimentel.domain.entities

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class CategorySuggestionTest {

    @Test
    fun `CategorySuggestion must not contain any null properties`() {
        val name = "name"

        val categorySuggestion = CategorySuggestion(name)

        assertNotNull(categorySuggestion)
        assertEquals(categorySuggestion.name, name)
    }
}
