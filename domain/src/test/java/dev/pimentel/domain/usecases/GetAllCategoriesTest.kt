package dev.pimentel.domain.usecases

import dev.pimentel.domain.repositories.CategoriesRepository
import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetAllCategoriesTest {

    private val categoriesRepository = mockk<CategoriesRepository>()
    private lateinit var useCase: GetAllCategories

    @BeforeEach
    fun `setup subject`() {
        useCase = GetAllCategories(categoriesRepository)
    }

    @Test
    fun `should return a list of categories`() = runBlocking {
        val categories = listOf(
            "name1",
            "name2",
        )

        coEvery { categoriesRepository.getAllCategories() } returns categories

        assertEquals(useCase(NoParams), categories)

        coVerify(exactly = 1) { categoriesRepository.getAllCategories() }
        confirmVerified(categoriesRepository)
    }
}
