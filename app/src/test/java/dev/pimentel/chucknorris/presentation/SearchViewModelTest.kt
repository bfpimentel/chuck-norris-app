package dev.pimentel.chucknorris.presentation

import dev.pimentel.chucknorris.presentation.search.SearchContract
import dev.pimentel.chucknorris.presentation.search.SearchViewModel
import dev.pimentel.chucknorris.testshared.ViewModelTest
import dev.pimentel.domain.usecases.GetCategorySuggestions
import dev.pimentel.domain.usecases.GetLastSearchTerms
import dev.pimentel.domain.usecases.HandleSearchTermSaving
import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SearchViewModelTest : ViewModelTest<SearchContract.ViewModel>() {

    private val getCategorySuggestions = mockk<GetCategorySuggestions>()
    private val handleSearchTermSaving = mockk<HandleSearchTermSaving>()
    private val getLastSearchTerms = mockk<GetLastSearchTerms>()
    override lateinit var viewModel: SearchContract.ViewModel

    override fun `setup subject`() {
        viewModel = SearchViewModel(
            getCategorySuggestions,
            handleSearchTermSaving,
            getLastSearchTerms,
            getErrorMessage,
            schedulerProvider
        )
    }

    @Test
    fun `should get category suggestions and get last search terms when initializing`() {
        val categorySuggestions = listOf(
            "name1",
            "name2"
        )
        val lastSearchTerms = listOf(
            "term1",
            "term2"
        )

        every { getCategorySuggestions(NoParams) } returns Single.just(categorySuggestions)
        every { getLastSearchTerms(NoParams) } returns Single.just(lastSearchTerms)

        viewModel.setupSearch()
        testScheduler.triggerActions()

        assertEquals(viewModel.categorySuggestions().value, categorySuggestions)
        assertEquals(viewModel.searchTerms().value, lastSearchTerms)

        verify(exactly = 1) {
            getCategorySuggestions(NoParams)
            getLastSearchTerms(NoParams)
        }
        confirmVerified(getCategorySuggestions, handleSearchTermSaving, getLastSearchTerms)
    }

    @Test
    fun `should save search term`() {
        val term = "term"
        val handleSearchTermSavingParams = HandleSearchTermSaving.Params(term)

        every { handleSearchTermSaving(handleSearchTermSavingParams) } returns Completable.complete()

        viewModel.saveSearchTerm(term)
        testScheduler.triggerActions()

        verify(exactly = 1) { handleSearchTermSaving(handleSearchTermSavingParams) }
        confirmVerified(getCategorySuggestions, handleSearchTermSaving, getLastSearchTerms)
    }
}
