package dev.pimentel.domain.usecases

import dev.pimentel.domain.repositories.CategoriesRepository
import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetAllCategoriesNamesTest {

    private val categoriesRepository = mockk<CategoriesRepository>()
    private lateinit var useCase: GetAllCategoriesNames

    @BeforeEach
    fun `setup subject`() {
        useCase = GetAllCategoriesNames(categoriesRepository)
    }

    @Test
    fun `should return a list of categories names`() = runBlocking {
        val categoriesNames = listOf(
            "name1",
            "name2"
        )

        coEvery { categoriesRepository.getAllCategoriesNames() } returns categoriesNames

        useCase(NoParams)

        coVerify(exactly = 1) { categoriesRepository.getAllCategoriesNames() }
        confirmVerified(categoriesRepository)
    }
}
