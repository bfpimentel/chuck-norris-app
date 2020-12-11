package dev.pimentel.domain.usecases

import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetCategorySuggestionsTest {

    private val fetchAllCategories = mockk<GetAllCategories>()
    private val fetchAllCategoriesNames = mockk<GetAllCategoriesNames>()
    private val saveAllCategories = mockk<SaveAllCategories>()
    private lateinit var useCase: GetCategorySuggestions

    @BeforeEach
    fun `setup subject`() {
        useCase = GetCategorySuggestions(fetchAllCategories)
    }

    @Test
    fun `should return a categories list with 8 random items`() = runBlocking {
        coEvery { fetchAllCategories(NoParams) } returns allCategories

        val result = useCase(NoParams)

        assertTrue(result.size == 8)
        result.forEach { suggestion -> assertTrue(allCategories.contains(suggestion)) }

        coVerify(exactly = 1) {
            fetchAllCategories(NoParams)
        }
        confirmVerified(
            fetchAllCategories,
            fetchAllCategoriesNames,
            saveAllCategories
        )
    }

    @Test
    fun `should return a categories list with less than 8 random items`() = runBlocking {
        coEvery { fetchAllCategories(NoParams) } returns allCategories.subList(0, 4)

        val result = useCase(NoParams)

        assertTrue(result.size == 4)
        result.forEach { suggestion -> assertTrue(allCategories.contains(suggestion)) }

        coVerify(exactly = 1) {
            fetchAllCategories(NoParams)
        }
        confirmVerified(
            fetchAllCategories,
            fetchAllCategoriesNames,
            saveAllCategories
        )
    }

    private companion object {
        val allCategories = listOf(
            "name0",
            "name1",
            "name2",
            "name3",
            "name4",
            "name5",
            "name6",
            "name7",
            "name8",
            "name9"
        )
    }
}
