package dev.pimentel.domain.usecases

import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SaveAndGetCategoriesSuggestionsTest {

    private val getAllCategoriesNames = mockk<GetAllCategoriesNames>()
    private val saveAllCategories = mockk<SaveAllCategories>()
    private val getCategorySuggestions = mockk<GetCategorySuggestions>()
    private lateinit var useCase: SaveAndGetCategoriesSuggestions

    @BeforeEach
    fun `setup subject`() {
        useCase = SaveAndGetCategoriesSuggestions(
            getAllCategoriesNames,
            saveAllCategories,
            getCategorySuggestions
        )
    }

    @Test
    fun `should return a list of category suggestions after getting their names and saving them`() =
        runBlocking {
            val saveAllCategoriesParams = SaveAllCategories.Params(categoriesNames)

            coEvery { getAllCategoriesNames(NoParams) } returns categoriesNames
            coEvery { saveAllCategories(saveAllCategoriesParams) } just runs
            coEvery { getCategorySuggestions(NoParams) } returns categorySuggestions

            assertEquals(useCase(NoParams), categorySuggestions)

            coVerify(exactly = 1) {
                getAllCategoriesNames(NoParams)
                saveAllCategories(saveAllCategoriesParams)
                getCategorySuggestions(NoParams)
            }
            confirmVerified(getAllCategoriesNames, saveAllCategories, getCategorySuggestions)
        }

    private companion object {
        val categoriesNames = listOf(
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

        val categorySuggestions = listOf(
            "name0",
            "name1",
            "name2",
            "name3",
            "name4",
            "name5",
            "name6",
            "name7"
        )
    }
}
