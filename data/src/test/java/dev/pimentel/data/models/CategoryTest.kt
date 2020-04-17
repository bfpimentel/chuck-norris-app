package dev.pimentel.data.models

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class CategoryTest {

    @Test
    fun `Category must not contain any null properties`() {
        val category = Category("name")

        assertNotNull(category.name)
    }
}
