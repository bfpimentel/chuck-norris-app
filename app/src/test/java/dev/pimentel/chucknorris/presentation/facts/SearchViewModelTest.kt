package dev.pimentel.chucknorris.presentation.facts

import dev.pimentel.chucknorris.presentation.search.SearchContract
import dev.pimentel.chucknorris.presentation.search.SearchViewModel
import dev.pimentel.chucknorris.testshared.ViewModelTest
import dev.pimentel.domain.usecases.GetCategorySuggestions
import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SearchViewModelTest : ViewModelTest<SearchContract.ViewModel>() {

    private val getCategorySuggestions = mockk<GetCategorySuggestions>()
    override lateinit var viewModel: SearchContract.ViewModel

    override fun `setup subject`() {
        viewModel = SearchViewModel(
            getCategorySuggestions,
            getErrorMessage,
            schedulerProvider
        )
    }

    @Test
    fun `should post value on category suggestions after getting them successfully`() {
        val categorySuggestions = listOf(
            CategorySuggestion("name1"),
            CategorySuggestion("name2")
        )

        every { getCategorySuggestions(NoParams) } returns Single.just(categorySuggestions)

        viewModel.getCategorySuggestions()
        testScheduler.triggerActions()

        assertEquals(viewModel.categorySuggestions().value, categorySuggestions)

        verify(exactly = 1) { getCategorySuggestions(NoParams) }
        confirmVerified(getCategorySuggestions, getErrorMessage)
    }
}
