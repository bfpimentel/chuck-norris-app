package dev.pimentel.chucknorris.presentation.search

import dev.pimentel.chucknorris.presentation.search.SearchContract
import dev.pimentel.chucknorris.presentation.search.SearchViewModel
import dev.pimentel.chucknorris.shared.navigator.NavigatorRouter
import dev.pimentel.chucknorris.testshared.ViewModelTest
import dev.pimentel.domain.usecases.AreCategoriesStored
import dev.pimentel.domain.usecases.GetCategorySuggestions
import dev.pimentel.domain.usecases.GetLastSearchTerms
import dev.pimentel.domain.usecases.HandleSearchTermSaving
import dev.pimentel.domain.usecases.SaveAndGetCategoriesSuggestions
import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
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

    override fun `setup subject`() {
        viewModel = SearchViewModel(
            navigator,
            areCategoriesStored,
            saveAndGetCategoriesSuggestions,
            getCategorySuggestions,
            handleSearchTermSaving,
            getLastSearchTerms,
            getErrorMessage,
            schedulerProvider
        )
    }

    @Test
    @DisplayName(
        """should save and get category suggestions when categories are not stored and 
            get search terms with one being equal to one category"""
    )
    fun test1() {
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

        every { areCategoriesStored(NoParams) } returns Single.just(false)
        every { saveAndGetCategoriesSuggestions(NoParams) } returns Single.just(categorySuggestions)
        every { getLastSearchTerms(NoParams) } returns Single.just(lastSearchTerms)

        assertNull(viewModel.isLoading().value)
        assertNull(viewModel.isNotLoading().value)

        viewModel.getCategorySuggestionsAndSearchTerms()
        testScheduler.triggerActions()

        assertNotNull(viewModel.isLoading().value)
        assertNotNull(viewModel.isNotLoading().value)

        assertEquals(viewModel.categorySuggestions().value, categorySuggestions)
        assertEquals(viewModel.searchTerms().value, lastSearchTerms)
        assertEquals(viewModel.selectedSuggestionIndex().value, 0)

        verify(exactly = 1) {
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
    fun `should just get category suggestions and last search terms`() {
        val categorySuggestions = listOf(
            "name1",
            "name2"
        )
        val lastSearchTerms = listOf(
            "term1",
            "term2"
        )

        every { areCategoriesStored(NoParams) } returns Single.just(true)
        every { getCategorySuggestions(NoParams) } returns Single.just(categorySuggestions)
        every { getLastSearchTerms(NoParams) } returns Single.just(lastSearchTerms)

        viewModel.getCategorySuggestionsAndSearchTerms()
        testScheduler.triggerActions()

        assertEquals(viewModel.categorySuggestions().value, categorySuggestions)
        assertEquals(viewModel.searchTerms().value, lastSearchTerms)
        assertNull(viewModel.selectedSuggestionIndex().value)

        assertNull(viewModel.isLoading().value)
        assertNull(viewModel.isNotLoading().value)

        verify(exactly = 1) {
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
    fun `should just get category suggestions and last search terms with empty list of search terms`() {
        val categorySuggestions = listOf(
            "name1",
            "name2"
        )
        val lastSearchTerms = listOf<String>()

        every { areCategoriesStored(NoParams) } returns Single.just(true)
        every { getCategorySuggestions(NoParams) } returns Single.just(categorySuggestions)
        every { getLastSearchTerms(NoParams) } returns Single.just(lastSearchTerms)

        viewModel.getCategorySuggestionsAndSearchTerms()
        testScheduler.triggerActions()

        assertEquals(viewModel.categorySuggestions().value, categorySuggestions)
        assertEquals(viewModel.searchTerms().value, lastSearchTerms)
        assertNull(viewModel.selectedSuggestionIndex().value)

        assertNull(viewModel.isLoading().value)
        assertNull(viewModel.isNotLoading().value)

        verify(exactly = 1) {
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
    fun `should save search term`() {
        val term = "term"
        val handleSearchTermSavingParams = HandleSearchTermSaving.Params(term)

        every { handleSearchTermSaving(handleSearchTermSavingParams) } returns Completable.complete()
        every { navigator.pop() } just runs

        viewModel.saveSearchTerm(term)
        testScheduler.triggerActions()

        verify(exactly = 1) {
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
