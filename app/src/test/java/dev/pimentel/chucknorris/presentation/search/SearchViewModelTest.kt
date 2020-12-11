package dev.pimentel.chucknorris.presentation.search

import dev.pimentel.chucknorris.presentation.search.data.SearchIntention
import dev.pimentel.chucknorris.presentation.search.data.SearchState
import dev.pimentel.chucknorris.shared.errorhandling.GetErrorMessage
import dev.pimentel.chucknorris.shared.navigator.NavigatorRouter
import dev.pimentel.chucknorris.shared.schedulerprovider.DispatchersProvider
import dev.pimentel.chucknorris.testshared.ViewModelTest
import dev.pimentel.domain.usecases.AreCategoriesStored
import dev.pimentel.domain.usecases.GetCategorySuggestions
import dev.pimentel.domain.usecases.GetLastSearchTerms
import dev.pimentel.domain.usecases.HandleSearchTermSaving
import dev.pimentel.domain.usecases.SaveAndGetCategoriesSuggestions
import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SearchViewModelTest : ViewModelTest<SearchContract.ViewModel>() {

    private val navigator = mockk<NavigatorRouter>()
    private val areCategoriesStored = mockk<AreCategoriesStored>()
    private val saveAndGetCategoriesSuggestions = mockk<SaveAndGetCategoriesSuggestions>()
    private val getCategorySuggestions = mockk<GetCategorySuggestions>()
    private val handleSearchTermSaving = mockk<HandleSearchTermSaving>()
    private val getLastSearchTerms = mockk<GetLastSearchTerms>()
    override lateinit var viewModel: SearchContract.ViewModel

    override fun `setup subject`(dispatchersProvider: DispatchersProvider) {
        viewModel = SearchViewModel(
            navigator,
            areCategoriesStored,
            saveAndGetCategoriesSuggestions,
            getCategorySuggestions,
            handleSearchTermSaving,
            getLastSearchTerms,
            getErrorMessage,
            dispatchersProvider
        )
    }

    @Test
    @DisplayName(
        """should save and get category suggestions when categories are not stored and 
            get search terms with one being equal to one category"""
    )
    fun test1() = testDispatcher.runBlockingTest {
        val categorySuggestions = listOf(
            "nameThatIsTheSameAsOneCategory",
            "name1",
            "name2"
        )
        val lastSearchTerms = listOf(
            "nameThatIsTheSameAsOneCategory",
            "term1",
            "term2"
        )

        coEvery { areCategoriesStored(NoParams) } returns false
        coEvery { saveAndGetCategoriesSuggestions(NoParams) } returns categorySuggestions
        coEvery { getLastSearchTerms(NoParams) } returns lastSearchTerms

        viewModel.publish(SearchIntention.GetCategorySuggestionsAndSearchTerms)

        val expectedSearchState = viewModel.state().value

        assertTrue(expectedSearchState is SearchState.Success)
        assertEquals(expectedSearchState.categorySuggestions, categorySuggestions)
        assertEquals(expectedSearchState.searchTermsEvent!!.value, lastSearchTerms)
        assertEquals(expectedSearchState.selectSuggestionEvent!!.value, 0)

        coVerify(exactly = 1) {
            areCategoriesStored(NoParams)
            saveAndGetCategoriesSuggestions(NoParams)
            getLastSearchTerms(NoParams)
        }
        confirmVerified(
            navigator,
            areCategoriesStored,
            saveAndGetCategoriesSuggestions,
            getCategorySuggestions,
            handleSearchTermSaving,
            getLastSearchTerms
        )
    }

    @Test
    fun `should just get category suggestions and last search terms`() =
        testDispatcher.runBlockingTest {
            val categorySuggestions = listOf(
                "name1",
                "name2"
            )
            val lastSearchTerms = listOf(
                "term1",
                "term2"
            )

            coEvery { areCategoriesStored(NoParams) } returns true
            coEvery { getCategorySuggestions(NoParams) } returns categorySuggestions
            coEvery { getLastSearchTerms(NoParams) } returns lastSearchTerms

            viewModel.publish(SearchIntention.GetCategorySuggestionsAndSearchTerms)

            val expectedSearchState = viewModel.state().value

            assertTrue(expectedSearchState is SearchState.Success)
            assertEquals(expectedSearchState.categorySuggestions, categorySuggestions)
            assertEquals(expectedSearchState.searchTermsEvent!!.value, lastSearchTerms)
            assertNull(expectedSearchState.selectSuggestionEvent?.value)

            coVerify(exactly = 1) {
                areCategoriesStored(NoParams)
                getCategorySuggestions(NoParams)
                getLastSearchTerms(NoParams)
            }
            confirmVerified(
                navigator,
                areCategoriesStored,
                saveAndGetCategoriesSuggestions,
                getCategorySuggestions,
                handleSearchTermSaving,
                getLastSearchTerms
            )
        }

    @Test
    fun `should just get category suggestions and last search terms with empty list of search terms`() =
        testDispatcher.runBlockingTest {
            val categorySuggestions = listOf(
                "name1",
                "name2"
            )
            val lastSearchTerms = listOf<String>()

            coEvery { areCategoriesStored(NoParams) } returns true
            coEvery { getCategorySuggestions(NoParams) } returns categorySuggestions
            coEvery { getLastSearchTerms(NoParams) } returns lastSearchTerms

            viewModel.publish(SearchIntention.GetCategorySuggestionsAndSearchTerms)

            val expectedSearchState = viewModel.state().value

            assertTrue(expectedSearchState is SearchState.Success)
            assertEquals(expectedSearchState.categorySuggestions, categorySuggestions)
            assertEquals(expectedSearchState.searchTermsEvent!!.value, lastSearchTerms)
            assertNull(expectedSearchState.selectSuggestionEvent?.value)

            coVerify(exactly = 1) {
                areCategoriesStored(NoParams)
                getCategorySuggestions(NoParams)
                getLastSearchTerms(NoParams)
            }
            confirmVerified(
                navigator,
                areCategoriesStored,
                saveAndGetCategoriesSuggestions,
                getCategorySuggestions,
                handleSearchTermSaving,
                getLastSearchTerms
            )
        }

    @Test
    fun `should post error message when failing to get category suggestions`() =
        testDispatcher.runBlockingTest {
            val error = IllegalArgumentException()
            val errorMessage = "errorMessage"
            val getErrorMessageParams = GetErrorMessage.Params(error)

            coEvery { areCategoriesStored(NoParams) } returns false
            coEvery { saveAndGetCategoriesSuggestions(NoParams) } throws error
            coEvery { getErrorMessage(getErrorMessageParams) } returns errorMessage

            viewModel.publish(SearchIntention.GetCategorySuggestionsAndSearchTerms)

            val expectedSearchState = viewModel.state().value

            assertTrue(expectedSearchState is SearchState.Error)
            assertEquals(expectedSearchState.errorEvent!!.value, errorMessage)
            assertTrue(expectedSearchState.hasError)

            coVerify(exactly = 1) {
                areCategoriesStored(NoParams)
                saveAndGetCategoriesSuggestions(NoParams)
                getErrorMessage(getErrorMessageParams)
            }
            confirmVerified(
                navigator,
                areCategoriesStored,
                saveAndGetCategoriesSuggestions,
                getCategorySuggestions,
                handleSearchTermSaving,
                getLastSearchTerms
            )
        }

    @Test
    fun `should save search term`() = testDispatcher.runBlockingTest {
        val term = "term"
        val handleSearchTermSavingParams = HandleSearchTermSaving.Params(term)

        coEvery { handleSearchTermSaving(handleSearchTermSavingParams) } just runs
        coEvery { navigator.pop() } just runs

        viewModel.publish(SearchIntention.SaveSearchTerm(term = term))

        coVerify(exactly = 1) {
            handleSearchTermSaving(handleSearchTermSavingParams)
            navigator.pop()
        }
        confirmVerified(
            navigator,
            getCategorySuggestions,
            handleSearchTermSaving,
            getLastSearchTerms
        )
    }
}
