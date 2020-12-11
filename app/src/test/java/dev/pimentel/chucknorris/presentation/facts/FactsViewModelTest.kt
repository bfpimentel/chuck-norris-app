package dev.pimentel.chucknorris.presentation.facts

import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.presentation.facts.data.FactsIntention
import dev.pimentel.chucknorris.presentation.facts.data.FactsState
import dev.pimentel.chucknorris.presentation.facts.mappers.FactDisplay
import dev.pimentel.chucknorris.presentation.facts.mappers.FactViewDataMapper
import dev.pimentel.chucknorris.presentation.facts.mappers.ShareableFactMapper
import dev.pimentel.chucknorris.shared.errorhandling.GetErrorMessage
import dev.pimentel.chucknorris.shared.navigator.Navigator
import dev.pimentel.chucknorris.shared.dispatchersprovider.DispatchersProvider
import dev.pimentel.chucknorris.testshared.ViewModelTest
import dev.pimentel.domain.entities.Fact
import dev.pimentel.domain.usecases.GetFacts
import dev.pimentel.domain.usecases.GetSearchTerm
import dev.pimentel.domain.usecases.shared.NoParams
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FactsViewModelTest : ViewModelTest<FactsContract.ViewModel>() {

    private val navigator = mockk<Navigator>()
    private val factDisplayMapper = mockk<FactViewDataMapper>()
    private val shareableFactMapper = mockk<ShareableFactMapper>()
    private val getFacts = mockk<GetFacts>()
    private val getSearchTerm = mockk<GetSearchTerm>()
    override lateinit var viewModel: FactsContract.ViewModel

    override fun `setup subject`(dispatchersProvider: DispatchersProvider) {
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
    fun `should navigate to search`() = testDispatcher.runBlockingTest {
        coEvery { navigator.navigate(R.id.facts_fragment_to_search_fragment) } just runs

        viewModel.publish(FactsIntention.NavigateToSearch)

        coVerify(exactly = 1) { navigator.navigate(R.id.facts_fragment_to_search_fragment) }
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
    fun `should get facts and map them to facts displays after getting them successfully`() =
        testDispatcher.runBlockingTest {
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

            coEvery { getSearchTerm(NoParams) } returns term
            coEvery { getFacts(getFactsParams) } returns facts
            coEvery { factDisplayMapper.map(facts) } returns factsDisplays

            viewModel.publish(FactsIntention.GetLastSearchAndFacts)

            val expected = viewModel.state().value

            assertTrue(expected is FactsState.WithFacts)
            assertEquals(expected.factsEvent!!.value, factsDisplays)
            assertTrue(expected.hasFacts)
            assertEquals(expected.searchTerm, term)

            coVerify(exactly = 1) {
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
    fun `should get search term and an empty list of facts`() = testDispatcher.runBlockingTest {
        val term = "term"
        val getFactsParams = GetFacts.Params(term)

        val facts = listOf<Fact>()

        coEvery { getSearchTerm(NoParams) } returns term
        coEvery { getFacts(getFactsParams) } returns facts

        viewModel.publish(FactsIntention.GetLastSearchAndFacts)

        val expected = viewModel.state().value

        assertTrue(expected is FactsState.Empty)
        assertTrue(expected.isEmpty)
        assertEquals(expected.searchTerm, term)

        coVerify(exactly = 1) {
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
    fun `should post value on error after failing to get facts`() = testDispatcher.runBlockingTest {
        val term = "term"
        val getFactsParams = GetFacts.Params(term)
        val error = IllegalArgumentException()
        val getErrorMessageParams = GetErrorMessage.Params(error)
        val errorMessage = "errorMessage"

        coEvery { getSearchTerm(NoParams) } returns term
        coEvery { getFacts(getFactsParams) } throws error
        coEvery { getErrorMessage(getErrorMessageParams) } returns errorMessage

        viewModel.publish(FactsIntention.GetLastSearchAndFacts)

        val expected = viewModel.state().value

        assertTrue(expected is FactsState.Error)
        assertEquals(expected.errorEvent!!.value, errorMessage)
        assertTrue(expected.hasError)

        coVerify(exactly = 1) {
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
    fun `should post first access search term could not be found`() =
        testDispatcher.runBlockingTest {
            coEvery { getSearchTerm(NoParams) } throws GetSearchTerm.SearchTermNotFoundException

            viewModel.publish(FactsIntention.GetLastSearchAndFacts)

            val expected = viewModel.state().value

            assertTrue(expected is FactsState.FirstAccess)
            assertTrue(expected.isFirstAccess)

            coVerify(exactly = 1) {
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

//    @Test
//    fun `should get shareable fact`() = testDispatcher.runBlockingTest {
//        val term = "term"
//        val getFactsParams = GetFacts.Params(term)
//
//        val facts = listOf(
//            Fact("id1", "category1", "url1", "value1"),
//            Fact("id2", "category2", "url2", "value2")
//        )
//
//        val factsDisplays = listOf(
//            FactDisplay("id1", "Category1", "value1", R.dimen.text_large),
//            FactDisplay("id2", "Category2", "value2", R.dimen.text_large)
//        )
//
//        val shareableFact = ShareableFact(
//            "url1",
//            "value1"
//        )
//
//        coEvery { getSearchTerm(NoParams) } returns term
//        coEvery { getFacts(getFactsParams) } returns facts
//        coEvery { factDisplayMapper.map(facts) } returns factsDisplays
//        coEvery { shareableFactMapper.map(facts.first()) } returns shareableFact
//
//        val emissions = mutableListOf<FactsState>()
//        val job = launch { viewModel.state().toList(emissions) }
//
//        viewModel.publish(FactsIntention.GetLastSearchAndFacts)
//        advanceTimeBy(2000L)
//
//        val expectedFactsState = emissions.last()
//
//        assertTrue(expectedFactsState is FactsState.WithFacts)
//        assertEquals(expectedFactsState.factsEvent!!.value, factsDisplays)
//        assertTrue(expectedFactsState.hasFacts)
//        assertEquals(expectedFactsState.searchTerm, term)
//
//        viewModel.publish(FactsIntention.ShareFact(id = factsDisplays.first().id))
//        advanceTimeBy(2000L)
//
//        val expectedShareState = emissions.last()
//
//        assertTrue(expectedFactsState is FactsState.Share)
//        assertEquals(expectedShareState.factsEvent!!.value, shareableFact)
//
//        coVerify(exactly = 1) {
//            getSearchTerm(NoParams)
//            getFacts(getFactsParams)
//            factDisplayMapper.map(facts)
//            shareableFactMapper.map(facts.first())
//        }
//        confirmVerified(
//            navigator,
//            factDisplayMapper,
//            shareableFactMapper,
//            getSearchTerm,
//            getFacts,
//            getErrorMessage
//        )
//
//        job.cancel()
//    }

    // TODO: Need to insert test with same search term
}
