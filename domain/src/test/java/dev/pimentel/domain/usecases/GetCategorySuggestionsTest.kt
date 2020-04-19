package dev.pimentel.domain.usecases

import dev.pimentel.data.models.Category
import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetCategorySuggestionsTest {

    private val fetchAllCategories = mockk<FetchAllCategories>()
    private val fetchAllCategoriesNames = mockk<FetchAllCategoriesNames>()
    private val saveAllCategories = mockk<SaveAllCategories>()
    private val shuffleList = mockk<ShuffleList>()
    private lateinit var getCategorySuggestions: GetCategorySuggestions

    @BeforeEach
    @Test
    fun `should setup subject and it must not be null`() {
        getCategorySuggestions = GetCategorySuggestions(
            fetchAllCategories,
            fetchAllCategoriesNames,
            saveAllCategories,
            shuffleList
        )

        assertNotNull(getCategorySuggestions)
    }

    @Test
    fun `should return a categories list with 8 random items when there are already saved items`() {
        val shuffleListParams = ShuffleList.Params(allCategories)

        every { fetchAllCategories(NoParams) } returns Single.just(allCategories)
        every { shuffleList(shuffleListParams) } returns allCategories

        getCategorySuggestions(NoParams)
            .test()
            .assertNoErrors()
            .assertResult(categorySuggestions)

        verify(exactly = 1) {
            fetchAllCategories(NoParams)
            shuffleList(shuffleListParams)
        }
        confirmVerified(fetchAllCategories, fetchAllCategoriesNames, saveAllCategories, shuffleList)
    }

    @Test
    fun `should return a categories list with 8 random items after fetching its names and saving them as categories when there are not saved items`() {
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
        val saveAllCategoriesParams = SaveAllCategories.Params(categoriesNames)
        val shuffleListParams = ShuffleList.Params(allCategories)

        every { fetchAllCategories(NoParams) } returns
                Single.just(listOf()) andThen
                Single.just(allCategories)
        every { fetchAllCategoriesNames(NoParams) } returns Single.just(categoriesNames)
        every { saveAllCategories(saveAllCategoriesParams) } returns Completable.complete()
        every { shuffleList(shuffleListParams) } returns allCategories

        getCategorySuggestions(NoParams)
            .test()
            .assertNoErrors()
            .assertResult(categorySuggestions)

        verify(exactly = 2) { fetchAllCategories(NoParams) }
        verify(exactly = 1) {
            fetchAllCategoriesNames(NoParams)
            saveAllCategories(saveAllCategoriesParams)
            shuffleList(shuffleListParams)
        }
        confirmVerified(fetchAllCategories, fetchAllCategoriesNames, saveAllCategories, shuffleList)
    }

    private companion object {
        val allCategories = listOf(
            Category("name0"),
            Category("name1"),
            Category("name2"),
            Category("name3"),
            Category("name4"),
            Category("name5"),
            Category("name6"),
            Category("name7"),
            Category("name8"),
            Category("name9")
        )

        val categorySuggestions = listOf(
            CategorySuggestion("name0"),
            CategorySuggestion("name1"),
            CategorySuggestion("name2"),
            CategorySuggestion("name3"),
            CategorySuggestion("name4"),
            CategorySuggestion("name5"),
            CategorySuggestion("name6"),
            CategorySuggestion("name7")
        )
    }
}
