package dev.pimentel.domain.usecases

import dev.pimentel.domain.repositories.CategoriesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SaveAllCategoriesTest {

    private val categoriesRepository = mockk<CategoriesRepository>()
    private lateinit var useCase: SaveAllCategories

    @BeforeEach
    fun `setup subject`() {
        useCase = SaveAllCategories(categoriesRepository)
    }

    @Test
    fun `should route saveAllCategories call to categoriesRepository after mapping the  to categories and then just complete`() =
        runBlocking {
            val categories = listOf(
                "name1",
                "name2"
            )

            coEvery { categoriesRepository.saveAllCategories(categories) } just runs

            useCase(SaveAllCategories.Params(categories))

            coVerify(exactly = 1) { categoriesRepository.saveAllCategories(categories) }
            confirmVerified(categoriesRepository)
        }
}
