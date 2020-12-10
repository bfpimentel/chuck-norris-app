package dev.pimentel.chucknorris.presentation.facts

import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.presentation.facts.data.FactsState
import dev.pimentel.chucknorris.presentation.facts.mappers.FactDisplay
import dev.pimentel.chucknorris.presentation.facts.mappers.FactDisplayMapper
import dev.pimentel.chucknorris.presentation.facts.mappers.ShareableFact
import dev.pimentel.chucknorris.presentation.facts.mappers.ShareableFactMapper
import dev.pimentel.chucknorris.shared.errorhandling.GetErrorMessage
import dev.pimentel.chucknorris.shared.navigator.Navigator
import dev.pimentel.chucknorris.testshared.ViewModelTest
import dev.pimentel.domain.entities.Fact
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
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FactsViewModelTest : ViewModelTest<FactsContract.ViewModel>() {

    private val navigator = mockk<Navigator>()
    private val factDisplayMapper = mockk<FactDisplayMapper>()
    private val shareableFactMapper = mockk<ShareableFactMapper>()
    private val getFacts = mockk<GetFacts>()
    private val getSearchTerm = mockk<GetSearchTerm>()
    override lateinit var viewModel: FactsContract.ViewModel

    override fun `setup subject`() {
        viewModel = FactsViewModel(
            navigator,
            factDisplayMapper,
            shareableFactMapper,
            getSearchTerm,
            getFacts,
            getErrorMessage,
            dispatchersProvider
        )
    }

    @Test
    fun `should navigate to search`() {
        every { navigator.navigate(R.id.facts_fragment_to_search_fragment) } just runs

        viewModel.navigateToSearch()

        verify(exactly = 1) { navigator.navigate(R.id.facts_fragment_to_search_fragment) }
        confirmVerified(
            navigator,
            factDisplayMapper,
            shareableFactMapper,
            getSearchTerm,
            getFacts,
            getErrorMessage
        )
    }

    @Test
    fun `should get facts and map them to facts displays after getting them successfully`() {
        val term = "term"
        val getFactsParams = GetFacts.Params(term)

        val facts = listOf(
            Fact("id1", "category1", "url1", "smallValue"),
            Fact(
                "id2",
                "category2",
                "url2",
                "bigValuebigValuebigValuebigValuebigValuebigValuebigValuebigValuebigValuebigValuebigValue"
            )
        )

        val factsDisplays = listOf(
            FactDisplay("id1", "Category1", "smallValue", R.dimen.text_large),
            FactDisplay(
                "id2",
                "Category2",
                "bigValuebigValuebigValuebigValuebigValuebigValuebigValuebigValuebigValuebigValuebigValue",
                R.dimen.text_normal
            )
        )

        every { getSearchTerm(NoParams) } returns Single.just(term)
        every { getFacts(getFactsParams) } returns Single.just(facts)
        every { factDisplayMapper.map(facts) } returns factsDisplays

        viewModel.getSearchTermAndFacts()
        testScheduler.triggerActions()

        val expected = viewModel.factsState().value!!

        assertTrue(expected is FactsState.WithFacts)
        assertEquals(expected.facts, factsDisplays)
        assertTrue(expected.hasFacts)
        assertEquals(expected.searchTerm, term)

        verify(exactly = 1) {
            getSearchTerm(NoParams)
            getFacts(getFactsParams)
            factDisplayMapper.map(facts)
        }
        confirmVerified(
            navigator,
            factDisplayMapper,
            shareableFactMapper,
            getSearchTerm,
            getFacts,
            getErrorMessage
        )
    }

    @Test
    fun `should get search term and an empty list of facts`() {
        val term = "term"
        val getFactsParams = GetFacts.Params(term)

        val facts = listOf<Fact>()

        every { getSearchTerm(NoParams) } returns Single.just(term)
        every { getFacts(getFactsParams) } returns Single.just(facts)

        viewModel.getSearchTermAndFacts()
        testScheduler.triggerActions()

        val expected = viewModel.factsState().value!!

        assertTrue(expected is FactsState.Empty)
        assertTrue(expected.isEmpty)
        assertEquals(expected.searchTerm, term)

        verify(exactly = 1) {
            getSearchTerm(NoParams)
            getFacts(getFactsParams)
        }
        confirmVerified(
            navigator,
            factDisplayMapper,
            shareableFactMapper,
            getSearchTerm,
            getFacts,
            getErrorMessage
        )
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

        viewModel.getSearchTermAndFacts()
        testScheduler.triggerActions()

        val expected = viewModel.factsState().value!!

        assertTrue(expected is FactsState.Error)
        assertEquals(expected.errorMessage, errorMessage)
        assertTrue(expected.hasError)

        verify(exactly = 1) {
            getSearchTerm(NoParams)
            getFacts(getFactsParams)
            getErrorMessage(getErrorMessageParams)
        }
        confirmVerified(
            navigator,
            factDisplayMapper,
            shareableFactMapper,
            getSearchTerm,
            getFacts,
            getErrorMessage
        )
    }

    @Test
    fun `should post first access search term could not be found`() {
        every { getSearchTerm(NoParams) } returns Single.error(GetSearchTerm.SearchTermNotFoundException())

        viewModel.getSearchTermAndFacts()
        testScheduler.triggerActions()

        val expected = viewModel.factsState().value!!

        assertTrue(expected is FactsState.FirstAccess)
        assertTrue(expected.isFirstAccess)

        verify(exactly = 1) {
            getSearchTerm(NoParams)
        }
        confirmVerified(
            navigator,
            factDisplayMapper,
            shareableFactMapper,
            getSearchTerm,
            getFacts,
            getErrorMessage
        )
    }


    @Test
    fun `should get shareable fact`() {
        val term = "term"
        val getFactsParams = GetFacts.Params(term)

        val facts = listOf(
            Fact("id1", "category1", "url1", "value1"),
            Fact("id2", "category2", "url2", "value2")
        )

        val factsDisplays = listOf(
            FactDisplay("id1", "Category1", "value1", R.dimen.text_large),
            FactDisplay("id2", "Category2", "value2", R.dimen.text_large)
        )

        val shareableFact = ShareableFact(
            "url1",
            "value1"
        )

        every { getSearchTerm(NoParams) } returns Single.just(term)
        every { getFacts(getFactsParams) } returns Single.just(facts)
        every { factDisplayMapper.map(facts) } returns factsDisplays
        every { shareableFactMapper.map(facts.first()) } returns shareableFact

        viewModel.getSearchTermAndFacts()
        testScheduler.triggerActions()

        val expectedFactsState = viewModel.factsState().value!!

        assertTrue(expectedFactsState is FactsState.WithFacts)
        assertEquals(expectedFactsState.facts, factsDisplays)
        assertTrue(expectedFactsState.hasFacts)
        assertEquals(expectedFactsState.searchTerm, term)

        viewModel.getShareableFact(factsDisplays.first().id)

        assertEquals(viewModel.shareableFact().value, shareableFact)

        verify(exactly = 1) {
            getSearchTerm(NoParams)
            getFacts(getFactsParams)
            factDisplayMapper.map(facts)
            shareableFactMapper.map(facts.first())
        }
        confirmVerified(
            navigator,
            factDisplayMapper,
            shareableFactMapper,
            getSearchTerm,
            getFacts,
            getErrorMessage
        )
    }
}
