package dev.pimentel.chucknorris.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.pimentel.chucknorris.presentation.search.data.SearchIntention
import dev.pimentel.chucknorris.presentation.search.data.SearchState
import dev.pimentel.chucknorris.shared.errorhandling.GetErrorMessage
import dev.pimentel.chucknorris.shared.mvi.toEvent
import dev.pimentel.chucknorris.shared.navigator.NavigatorRouter
import dev.pimentel.chucknorris.shared.schedulerprovider.DispatchersProvider
import dev.pimentel.domain.usecases.AreCategoriesStored
import dev.pimentel.domain.usecases.GetCategorySuggestions
import dev.pimentel.domain.usecases.GetLastSearchTerms
import dev.pimentel.domain.usecases.HandleSearchTermSaving
import dev.pimentel.domain.usecases.SaveAndGetCategoriesSuggestions
import dev.pimentel.domain.usecases.shared.NoParams
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

@Suppress("LongParameterList")
class SearchViewModel(
    private val navigator: NavigatorRouter,
    private val areCategoriesStored: AreCategoriesStored,
    private val saveAndGetCategoriesSuggestions: SaveAndGetCategoriesSuggestions,
    private val getCategorySuggestions: GetCategorySuggestions,
    private val handleSearchTermSaving: HandleSearchTermSaving,
    private val getLastSearchTerms: GetLastSearchTerms,
    private val getErrorMessage: GetErrorMessage,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel(), SearchContract.ViewModel {

    private val mutableState = MutableStateFlow<SearchState>(SearchState.Initial)
    private val publisher = MutableSharedFlow<SearchIntention>()

    init {
        publisher.onEach { intention ->
            viewModelScope.launch(dispatchersProvider.io) {
                handleIntentions(intention)
            }
        }.shareIn(viewModelScope, SharingStarted.Eagerly)
    }

    override fun state(): StateFlow<SearchState> = mutableState

    override fun publish(intention: SearchIntention) {
        viewModelScope.launch(dispatchersProvider.io) {
            publisher.emit(intention)
        }
    }

    private suspend fun handleIntentions(intention: SearchIntention) {
        when (intention) {
            is SearchIntention.GetCategorySuggestionsAndSearchTerms -> getCategorySuggestionsAndSearchTerms()
            is SearchIntention.SaveSearchTerm -> saveSearchTerm(term = intention.term)
        }
    }

    private suspend fun getCategorySuggestionsAndSearchTerms() {
        try {
            val areCategoriesStored = areCategoriesStored(NoParams)

            val suggestions = if (areCategoriesStored) {
                getCategorySuggestions(NoParams)
            } else {
                mutableState.value = SearchState.Loading(isLoading = true)
                saveAndGetCategoriesSuggestions(NoParams).also {
                    mutableState.value = SearchState.Loading(isLoading = false)
                }
            }

            val searchTerms = getLastSearchTerms(NoParams)

            val selectedSuggestionIndex = searchTerms
                .firstOrNull()
                ?.let { lastSearchTerm ->
                    suggestions.indexOfFirst { suggestion -> suggestion == lastSearchTerm }
                }?.takeIf { index -> index != NOT_FOUND_INDEX }

            mutableState.value = SearchState.Success(
                categorySuggestions = suggestions,
                searchTerms = searchTerms.toEvent(),
                selectSuggestionEvent = selectedSuggestionIndex?.toEvent()
            )
        } catch (error: Exception) {
            val errorMessage = getErrorMessage(GetErrorMessage.Params(error))
            mutableState.value = SearchState.Error(errorMessage = errorMessage.toEvent())
        }
    }

    private suspend fun saveSearchTerm(term: String) {
        handleSearchTermSaving(HandleSearchTermSaving.Params(term))
        mutableState.value = SearchState.NewSearch(newSearch = term.toEvent())
        navigator.pop()
    }

    private companion object {
        const val NOT_FOUND_INDEX = -1
    }
}
