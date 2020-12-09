package dev.pimentel.chucknorris.presentation.facts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.presentation.facts.data.FactsIntention
import dev.pimentel.chucknorris.presentation.facts.data.FactsState
import dev.pimentel.chucknorris.presentation.facts.mappers.FactDisplayMapper
import dev.pimentel.chucknorris.presentation.facts.mappers.ShareableFactMapper
import dev.pimentel.chucknorris.shared.errorhandling.GetErrorMessage
import dev.pimentel.chucknorris.shared.mvi.Event
import dev.pimentel.chucknorris.shared.mvi.Reducer
import dev.pimentel.chucknorris.shared.mvi.toEvent
import dev.pimentel.chucknorris.shared.navigator.NavigatorRouter
import dev.pimentel.chucknorris.shared.schedulerprovider.DispatchersProvider
import dev.pimentel.domain.entities.Fact
import dev.pimentel.domain.usecases.GetFacts
import dev.pimentel.domain.usecases.GetSearchTerm
import dev.pimentel.domain.usecases.shared.NoParams
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

@Suppress("LongParameterList")
class FactsViewModel(
    private val navigator: NavigatorRouter,
    private val factDisplayMapper: FactDisplayMapper,
    private val shareableFactMapper: ShareableFactMapper,
    private val getSearchTerm: GetSearchTerm,
    private val getFacts: GetFacts,
    private val getErrorMessage: GetErrorMessage,
    private val reducer: Reducer<FactsState>,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel(), FactsContract.ViewModel, Reducer<FactsState> by reducer {

    private var lastSearch: String? = null
    private lateinit var facts: List<Fact>

    private val publisher = MutableSharedFlow<FactsIntention>()

    init {
        publisher.onEach { intention ->
            viewModelScope.launch(dispatchersProvider.io) {
                handleIntentions(intention)
            }
        }.shareIn(viewModelScope, SharingStarted.Eagerly)
    }

    override fun state(): StateFlow<FactsState> = mutableState

    override fun publish(intention: FactsIntention) {
        viewModelScope.launch(dispatchersProvider.io) {
            publisher.emit(intention)
        }
    }

    private suspend fun handleIntentions(intention: FactsIntention) {
        when (intention) {
            is FactsIntention.GetLastSearchAndFacts -> getFacts()
            is FactsIntention.NewSearch -> getFacts(newSearchTerm = intention.term)
            is FactsIntention.NavigateToSearch -> navigateToSearch()
            is FactsIntention.ShareFact -> getShareableFact(id = intention.id)
        }
    }

    private suspend fun getFacts(newSearchTerm: String? = null) {
        updateState { copy(isLoading = true) }

//        mutableState.value = FactsState.Loading(true)

        try {
            if (lastSearch != null && lastSearch == newSearchTerm) {
                return
            }

            val searchTerm = newSearchTerm ?: getSearchTerm(NoParams)
            this.lastSearch = searchTerm

            val facts = getFacts(GetFacts.Params(searchTerm))

            this.facts = facts

            if (facts.isEmpty()) {
                updateState {
                    copy(
                        searchTerm = searchTerm,
                        emptyListEvent = Event.NoContent,
                        isLoading = false,
                    )
                }
//                mutableState.value = FactsState.Empty(searchTerm = searchTerm)
                return
            }

            updateState {
                copy(
                    facts = factDisplayMapper.map(facts),
                    searchTerm = searchTerm,
                )
            }
//            mutableState.value = FactsState.Success(
//                facts = factDisplayMapper.map(facts),
//                searchTerm = searchTerm,
//            )
        } catch (error: Exception) {
            if (error is GetSearchTerm.SearchTermNotFoundException) {
                updateState { copy(isFirstAccess = true) }
//                mutableState.value = FactsState.FirstAccess
            } else {
                val errorMessage = getErrorMessage(GetErrorMessage.Params(error))
                updateState { copy(errorEvent = errorMessage.toEvent()) }
//                mutableState.value = FactsState.Error(errorMessage = errorMessage)
            }
        } finally {
            updateState { copy(isLoading = false) }
        }
    }

    private fun navigateToSearch() {
        navigator.navigate(R.id.facts_fragment_to_search_fragment)
    }

    private suspend fun getShareableFact(id: String) {
        facts.first { it.id == id }
            .let(shareableFactMapper::map)
            .also { shareableFact ->
                updateState { copy(shareFactEvent = shareableFact.toEvent()) }
//                mutableState.value = FactsState.Share(shareableFact = shareableFact)
            }
    }
}
