package dev.pimentel.domain.usecases

import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.jupiter.api.Test

class SaveAndGetCategoriesSuggestionsTest : UseCaseTest<SaveAndGetCategoriesSuggestions>() {

    private val getAllCategoriesNames = mockk<GetAllCategoriesNames>()
    private val saveAllCategories = mockk<SaveAllCategories>()
    private val getCategorySuggestions = mockk<GetCategorySuggestions>()
    override lateinit var useCase: SaveAndGetCategoriesSuggestions

    override fun `setup subject`() {
        useCase = SaveAndGetCategoriesSuggestions(
            getAllCategoriesNames,
            saveAllCategories,
            getCategorySuggestions
        )
    }

    @Test
    fun `should return a list of category suggestions after getting their names and saving them`() {
        val saveAllCategoriesParams = SaveAllCategories.Params(categoriesNames)

        every { getAllCategoriesNames(NoParams) } returns Single.just(categoriesNames)
        every { saveAllCategories(saveAllCategoriesParams) } returns Completable.complete()
        every { getCategorySuggestions(NoParams) } returns Single.just(categorySuggestions)

        useCase(NoParams)
            .test()
            .assertNoErrors()
            .assertResult(categorySuggestions)

        verify(exactly = 1) {
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
