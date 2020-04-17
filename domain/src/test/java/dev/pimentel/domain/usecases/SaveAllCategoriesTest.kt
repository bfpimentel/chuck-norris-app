package dev.pimentel.domain.usecases

import dev.pimentel.data.models.Category
import dev.pimentel.data.repositories.CategoriesRepository
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SaveAllCategoriesTest {

    private val categoriesRepository = mockk<CategoriesRepository>()
    private lateinit var saveAllCategories: SaveAllCategories

    @BeforeEach
    @Test
    fun `should setup subject and it must not be null`() {
        saveAllCategories = SaveAllCategories(categoriesRepository)

        assertNotNull(saveAllCategories)
    }

    @Test
    fun `should route saveAllCategories call to categoriesRepository after mapping the  to categories and then just complete`() {
        val categoriesNames = listOf(
            "name1",
            "name2"
        )

        val categories = listOf(
            Category("name1"),
            Category("name2")
        )

        every { categoriesRepository.saveAllCategories(categories) } just runs

        saveAllCategories(SaveAllCategories.Params(categoriesNames))
            .test()
            .assertNoErrors()
            .assertComplete()

        verify(exactly = 1) { categoriesRepository.saveAllCategories(categories) }
        confirmVerified(categoriesRepository)
    }

    @Test
    fun `Params must contain a not null list of categories names`() {
        val categoriesNames = listOf(
            "name1",
            "name2"
        )

        val params = SaveAllCategories.Params(categoriesNames)

        assertNotNull(params.categoriesNames)
    }
}
