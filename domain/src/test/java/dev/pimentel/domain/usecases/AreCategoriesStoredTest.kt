package dev.pimentel.domain.usecases

import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AreCategoriesStoredTest {

    private val getAllCategories = mockk<GetAllCategories>()
    private lateinit var useCase: AreCategoriesStored

    @BeforeEach
    private fun `setup subject`() {
        useCase = AreCategoriesStored(getAllCategories)
    }

    @Test
    fun `should return false when there are no categories stored`() = runBlocking {
        coEvery { getAllCategories(NoParams) } returns listOf()

        assertFalse(useCase(NoParams))

        coVerify(exactly = 1) { getAllCategories(NoParams) }
        confirmVerified(getAllCategories)
    }

    @Test
    fun `should return true when there categories stored`() = runBlocking {
        coEvery { getAllCategories(NoParams) } returns listOf("category1")

        assertTrue(useCase(NoParams))

        coVerify(exactly = 1) { getAllCategories(NoParams) }
        confirmVerified(getAllCategories)
    }
}
