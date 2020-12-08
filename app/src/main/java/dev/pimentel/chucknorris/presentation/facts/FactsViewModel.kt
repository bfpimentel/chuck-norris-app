package dev.pimentel.chucknorris.presentation.facts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.presentation.facts.data.FactsIntention
import dev.pimentel.chucknorris.presentation.facts.data.FactsState
import dev.pimentel.chucknorris.presentation.facts.mappers.FactDisplayMapper
import dev.pimentel.chucknorris.presentation.facts.mappers.ShareableFactMapper
import dev.pimentel.chucknorris.shared.errorhandling.GetErrorMessage
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
) : ViewModel(), FactsContract.ViewModel,
    Reducer<FactsState> by reducer {

    private lateinit var facts: List<Fact>

    override fun state(): StateFlow<FactsState> = mutableState

    private val publisher = MutableSharedFlow<FactsIntention>()

    init {
        publisher.onEach { intention ->
            viewModelScope.launch(dispatchersProvider.io) {
                handleIntentions(intention)
            }
        }.shareIn(viewModelScope, SharingStarted.Eagerly)
    }

    override fun publish(intention: FactsIntention) {
        viewModelScope.launch(dispatchersProvider.io) {
            publisher.emit(intention)
        }
    }

    private suspend fun handleIntentions(intention: FactsIntention) {
        when (intention) {
            is FactsIntention.GetSearchTermsAndFacts -> getSearchTermAndFacts()
            is FactsIntention.NavigateToSearch -> navigateToSearch()
            is FactsIntention.ShareFact -> getShareableFact(id = intention.id)
        }
    }

    private suspend fun getSearchTermAndFacts() {
        updateState { copy(isLoading = true) }

        try {
            val searchTerm = getSearchTerm(NoParams)
            val facts = getFacts(GetFacts.Params(searchTerm))

            this.facts = facts

            if (facts.isEmpty()) {
                updateState {
                    copy(
                        searchTerm = searchTerm,
                        isEmpty = true,
                        isLoading = false,
                    )
                }
                return
            }

            updateState {
                copy(
                    facts = factDisplayMapper.map(facts),
                    searchTerm = searchTerm,
                )
            }
        } catch (error: Exception) {
            if (error is GetSearchTerm.SearchTermNotFoundException) {
                updateState { copy(isFirstAccess = true) }
            } else {
                val errorMessage = getErrorMessage(GetErrorMessage.Params(error))
                updateState { copy(errorEvent = errorMessage.toEvent()) }
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
            }
    }
}
