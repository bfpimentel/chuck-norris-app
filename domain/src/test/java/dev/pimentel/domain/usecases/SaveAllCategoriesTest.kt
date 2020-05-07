package dev.pimentel.domain.usecases

import dev.pimentel.domain.models.Category
import dev.pimentel.domain.repositories.CategoriesRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class SaveAllCategoriesTest : UseCaseTest<SaveAllCategories>() {

    private val categoriesRepository = mockk<CategoriesRepository>()
    override lateinit var useCase: SaveAllCategories

    override fun `setup subject`() {
        useCase = SaveAllCategories(categoriesRepository)
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

        useCase(SaveAllCategories.Params(categoriesNames))
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
