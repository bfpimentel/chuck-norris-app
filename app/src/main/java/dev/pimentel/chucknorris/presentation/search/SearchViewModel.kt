package dev.pimentel.chucknorris.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.pimentel.chucknorris.shared.errorhandling.GetErrorMessage
import dev.pimentel.chucknorris.shared.helpers.DisposablesHolder
import dev.pimentel.chucknorris.shared.helpers.DisposablesHolderImpl
import dev.pimentel.chucknorris.shared.navigator.NavigatorRouter
import dev.pimentel.chucknorris.shared.schedulerprovider.SchedulerProvider
import dev.pimentel.domain.usecases.AreCategoriesStored
import dev.pimentel.domain.usecases.GetCategorySuggestions
import dev.pimentel.domain.usecases.GetLastSearchTerms
import dev.pimentel.domain.usecases.HandleSearchTermSaving
import dev.pimentel.domain.usecases.SaveAndGetCategoriesSuggestions
import dev.pimentel.domain.usecases.shared.NoParams

@Suppress("LongParameterList")
class SearchViewModel(
    private val navigator: NavigatorRouter,
    private val areCategoriesStored: AreCategoriesStored,
    private val saveAndGetCategoriesSuggestions: SaveAndGetCategoriesSuggestions,
    private val getCategorySuggestions: GetCategorySuggestions,
    private val handleSearchTermSaving: HandleSearchTermSaving,
    private val getLastSearchTerms: GetLastSearchTerms,
    private val getErrorMessage: GetErrorMessage,
    schedulerProvider: SchedulerProvider
) : ViewModel(),
    DisposablesHolder by DisposablesHolderImpl(schedulerProvider),
    SearchContract.ViewModel {

    private val searchState = MutableLiveData<SearchState>()
    private val selectedSuggestionIndex = MutableLiveData<Int>()

    override fun searchState(): LiveData<SearchState> = searchState
    override fun selectedSuggestionIndex(): LiveData<Int> = selectedSuggestionIndex

    override fun onCleared() {
        super.onCleared()
        dispose()
    }

    override fun getCategorySuggestionsAndSearchTerms() {
        areCategoriesStored(NoParams)
            .flatMap { areStored ->
                if (areStored) {
                    getCategorySuggestions(NoParams)
                } else {
                    saveAndGetCategoriesSuggestions(NoParams)
                        .doOnSubscribe { searchState.postValue(SearchState.Loading(true)) }
                        .doFinally { searchState.postValue(SearchState.Loading(false)) }
                }
            }.flatMap { categorySuggestions ->
                getLastSearchTerms(NoParams).map { searchTerms ->
                    InitializeData(categorySuggestions, searchTerms)
                }
            }.compose(observeOnUIAfterSingleResult())
            .handle({ data ->
                searchState.postValue(SearchState.Success(data.suggestions, data.searchTerms))

                data.searchTerms
                    .firstOrNull()
                    ?.let { lastSearchTerm ->
                        data.suggestions
                            .indexOfFirst { suggestion -> suggestion == lastSearchTerm }
                    }?.also { index ->
                        if (index != NOT_FOUND_INDEX) {
                            selectedSuggestionIndex.postValue(index)
                        }
                    }
            }, { error ->
                searchState.postValue(
                    SearchState.Error(
                        getErrorMessage(GetErrorMessage.Params(error))
                    )
                )
            })
    }

    override fun saveSearchTerm(term: String) {
        handleSearchTermSaving(HandleSearchTermSaving.Params(term))
            .compose(observeOnUIAfterCompletableResult())
            .handle(navigator::pop) {}
    }

    private data class InitializeData(
        val suggestions: List<String>,
        val searchTerms: List<String>
    )

    private companion object {
        const val NOT_FOUND_INDEX = -1
    }
}
