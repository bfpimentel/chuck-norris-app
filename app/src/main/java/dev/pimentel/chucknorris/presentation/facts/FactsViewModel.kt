package dev.pimentel.chucknorris.presentation.facts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.presentation.facts.data.FactsIntention
import dev.pimentel.chucknorris.presentation.facts.data.FactsState
import dev.pimentel.chucknorris.presentation.facts.mappers.FactDisplayMapper
import dev.pimentel.chucknorris.presentation.facts.mappers.ShareableFactMapper
import dev.pimentel.chucknorris.shared.errorhandling.GetErrorMessage
import dev.pimentel.chucknorris.shared.mvi.toEvent
import dev.pimentel.chucknorris.shared.navigator.NavigatorRouter
import dev.pimentel.chucknorris.shared.schedulerprovider.DispatchersProvider
import dev.pimentel.domain.entities.Fact
import dev.pimentel.domain.usecases.GetFacts
import dev.pimentel.domain.usecases.GetSearchTerm
import dev.pimentel.domain.usecases.shared.NoParams
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val dispatchersProvider: DispatchersProvider
) : ViewModel(), FactsContract.ViewModel {

    private var lastSearch: String? = null
    private lateinit var facts: List<Fact>

    private val mutableState = MutableStateFlow<FactsState>(FactsState.Initial)
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
        try {
            if (lastSearch != null && lastSearch == newSearchTerm) {
                return
            }

            val searchTerm = newSearchTerm ?: getSearchTerm(NoParams)
            this.lastSearch = searchTerm

            mutableState.value = FactsState.Loading(isLoading = true)

            val facts = getFacts(GetFacts.Params(searchTerm))

            this.facts = facts

            if (facts.isEmpty()) {
                mutableState.value = FactsState.Empty(searchTerm = searchTerm)
                return
            }

            mutableState.value = FactsState.WithFacts(
                factsEvent = factDisplayMapper.map(facts).toEvent(),
                searchTerm = searchTerm,
            )
        } catch (@Suppress("TooGenericExceptionCaught") error: Exception) {
            if (error is GetSearchTerm.SearchTermNotFoundException) {
                mutableState.value = FactsState.FirstAccess
            } else {
                val errorMessage = getErrorMessage(GetErrorMessage.Params(error))
                mutableState.value = FactsState.Error(errorEvent = errorMessage.toEvent())
            }
        }
    }

    private suspend fun navigateToSearch() {
        navigator.navigate(R.id.facts_fragment_to_search_fragment)
    }

    private fun getShareableFact(id: String) {
        facts.first { it.id == id }
            .let(shareableFactMapper::map)
            .also { shareableFact ->
                mutableState.value = FactsState.Share(shareableFact = shareableFact)
            }
    }
}
