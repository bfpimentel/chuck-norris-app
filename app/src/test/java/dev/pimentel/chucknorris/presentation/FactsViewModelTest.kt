package dev.pimentel.chucknorris.presentation

import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.presentation.facts.FactsContract
import dev.pimentel.chucknorris.presentation.facts.FactsViewModel
import dev.pimentel.chucknorris.shared.navigator.Navigator
import dev.pimentel.chucknorris.testshared.ViewModelTest
import dev.pimentel.domain.entities.Fact
import dev.pimentel.domain.usecases.GetErrorMessage
import dev.pimentel.domain.usecases.GetFacts
import dev.pimentel.domain.usecases.GetSearchTerm
import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class FactsViewModelTest : ViewModelTest<FactsContract.ViewModel>() {

    private val navigator = mockk<Navigator>()
    private val getFacts = mockk<GetFacts>()
    private val getSearchTerm = mockk<GetSearchTerm>()
    override lateinit var viewModel: FactsContract.ViewModel

    override fun `setup subject`() {
        viewModel = FactsViewModel(
            navigator,
            getSearchTerm,
            getFacts,
            getErrorMessage,
            schedulerProvider
        )
    }

    @Test
    fun `should navigate to search`() {
        every { navigator.navigate(R.id.facts_fragment_to_search_fragment) } just runs

        viewModel.navigateToSearch()

        verify(exactly = 1) { navigator.navigate(R.id.facts_fragment_to_search_fragment) }
        confirmVerified(navigator, getSearchTerm, getFacts, getErrorMessage)
    }

    @Test
    fun `should get facts and map them to facts displays after getting them successfully`() {
        val term = "term"
        val getFactsParams = GetFacts.Params(term)

        val facts = listOf(
            Fact("category1", "url1", "smallValue"),
            Fact(
                "category2",
                "url2",
                "bigValuebigValuebigValuebigValuebigValuebigValuebigValuebigValuebigValuebigValuebigValue"
            )
        )

        val factsDisplays = listOf(
            FactsViewModel.FactDisplay("Category1", "smallValue", R.dimen.text_large),
            FactsViewModel.FactDisplay(
                "Category2",
                "bigValuebigValuebigValuebigValuebigValuebigValuebigValuebigValuebigValuebigValuebigValue",
                R.dimen.text_normal
            )
        )

        every { getSearchTerm(NoParams) } returns Single.just(term)
        every { getFacts(getFactsParams) } returns Single.just(facts)

        viewModel.setupFacts()
        testScheduler.triggerActions()

        assertEquals(viewModel.searchTerm().value, term)
        assertEquals(viewModel.facts().value, factsDisplays)

        verify(exactly = 1) {
            getSearchTerm(NoParams)
            getFacts(getFactsParams)
        }
        confirmVerified(navigator, getSearchTerm, getFacts, getErrorMessage)
    }

    @Test
    fun `should post value on error after failing to get facts`() {
        val term = "term"
        val getFactsParams = GetFacts.Params(term)
        val error = IllegalArgumentException()
        val getErrorMessageParams = GetErrorMessage.Params(error)
        val errorMessage = "errorMessage"

        every { getSearchTerm(NoParams) } returns Single.just(term)
        every { getFacts(getFactsParams) } returns Single.error(error)
        every { getErrorMessage(getErrorMessageParams) } returns errorMessage

        assertNull(viewModel.error().value)

        viewModel.setupFacts()
        testScheduler.triggerActions()

        assertEquals(viewModel.error().value, errorMessage)

        verify(exactly = 1) {
            getSearchTerm(NoParams)
            getFacts(getFactsParams)
            getErrorMessage(getErrorMessageParams)
        }
        confirmVerified(navigator, getSearchTerm, getFacts, getErrorMessage)
    }

    @Test
    fun `should post value on first access after failing to get search term`() {
        every { getSearchTerm(NoParams) } returns Single.error(GetSearchTerm.SearchTermNotFoundException())

        assertNull(viewModel.firstAccess().value)

        viewModel.setupFacts()
        testScheduler.triggerActions()

        assertNotNull(viewModel.firstAccess().value)

        verify(exactly = 1) {
            getSearchTerm(NoParams)
        }
        confirmVerified(navigator, getSearchTerm, getFacts, getErrorMessage)
    }

    @Test
    fun `FactDisplay must not contain any null properties`() {
        val factDisplay = FactsViewModel.FactDisplay(
            "category",
            "value",
            R.dimen.text_normal
        )

        assertNotNull(factDisplay.category)
        assertNotNull(factDisplay.value)
        assertNotNull(factDisplay.fontSize)
    }
}
