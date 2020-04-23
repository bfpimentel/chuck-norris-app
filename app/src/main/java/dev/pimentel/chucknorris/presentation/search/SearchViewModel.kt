package dev.pimentel.chucknorris.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.pimentel.chucknorris.shared.abstractions.BaseViewModel
import dev.pimentel.chucknorris.shared.navigator.NavigatorRouter
import dev.pimentel.chucknorris.shared.schedulerprovider.SchedulerProvider
import dev.pimentel.domain.usecases.AreCategoriesStored
import dev.pimentel.domain.usecases.GetCategorySuggestions
import dev.pimentel.domain.usecases.GetErrorMessage
import dev.pimentel.domain.usecases.GetLastSearchTerms
import dev.pimentel.domain.usecases.HandleSearchTermSaving
import dev.pimentel.domain.usecases.SaveAndGetCategoriesSuggestions
import dev.pimentel.domain.usecases.shared.NoParams
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class SearchViewModel(
    private val navigator: NavigatorRouter,
    private val areCategoriesStored: AreCategoriesStored,
    private val saveAndGetCategoriesSuggestions: SaveAndGetCategoriesSuggestions,
    private val getCategorySuggestions: GetCategorySuggestions,
    private val handleSearchTermSaving: HandleSearchTermSaving,
    private val getLastSearchTerms: GetLastSearchTerms,
    getErrorMessage: GetErrorMessage,
    schedulerProvider: SchedulerProvider
) : BaseViewModel(
    schedulerProvider,
    getErrorMessage
), SearchContract.ViewModel {

    private val categorySuggestions = MutableLiveData<List<String>>()
    private val searchTerms = MutableLiveData<List<String>>()
    private val selectedSuggestionIndex = MutableLiveData<Int>()

    override fun categorySuggestions(): LiveData<List<String>> = categorySuggestions
    override fun searchTerms(): LiveData<List<String>> = searchTerms
    override fun selectedSuggestionIndex(): LiveData<Int> = selectedSuggestionIndex

    override fun setupSearch() {
        Single.zip(
            areCategoriesStored(NoParams)
                .flatMap { areStored ->
                    if (areStored) {
                        getCategorySuggestions(NoParams)
                    } else {
                        saveAndGetCategoriesSuggestions(NoParams)
                            .doOnSubscribe { isLoading.postValue(Unit) }
                            .doFinally { isNotLoading.postValue(Unit) }
                    }
                },
            getLastSearchTerms(NoParams),
            BiFunction(::InitializeData)
        ).compose(observeOnUIAfterSingleResult())
            .handle({ data ->
                categorySuggestions.postValue(data.suggestions)
                searchTerms.postValue(data.searchTerms)

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
            }, ::postErrorMessage)
    }

    override fun saveSearchTerm(term: String) {
        handleSearchTermSaving(HandleSearchTermSaving.Params(term))
            .compose(observeOnUIAfterCompletableResult())
            .handle(navigator::pop, ::postErrorMessage)
    }

    private data class InitializeData(
        val suggestions: List<String>,
        val searchTerms: List<String>
    )

    private companion object {
        const val NOT_FOUND_INDEX = -1
    }
}
