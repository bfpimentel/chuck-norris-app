package dev.pimentel.chucknorris.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.pimentel.chucknorris.shared.abstractions.BaseViewModel
import dev.pimentel.chucknorris.shared.schedulerprovider.SchedulerProvider
import dev.pimentel.domain.usecases.GetCategorySuggestions
import dev.pimentel.domain.usecases.GetErrorMessage
import dev.pimentel.domain.usecases.GetLastSearchTerms
import dev.pimentel.domain.usecases.HandleSearchTermSaving
import dev.pimentel.domain.usecases.shared.NoParams
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class SearchViewModel(
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

    override fun categorySuggestions(): LiveData<List<String>> = categorySuggestions

    override fun searchTerms(): LiveData<List<String>> = searchTerms

    override fun initialize() {
        Single.zip(
            getCategorySuggestions(NoParams),
            getLastSearchTerms(NoParams),
            BiFunction(::InitializeData)
        ).compose(observeOnUIAfterSingleResult())
            .doOnSubscribe { isLoading.postValue(true) }
            .doFinally { isLoading.postValue(false) }
            .handle({ data ->
                categorySuggestions.postValue(data.suggestions)
                searchTerms.postValue(data.searchTerms)
            }, ::postErrorMessage)
    }

    override fun saveSearchTerm(term: String) {
        handleSearchTermSaving(HandleSearchTermSaving.Params(term))
            .compose(observeOnUIAfterCompletableResult())
            .handle({ }, ::postErrorMessage)
    }

    private data class InitializeData(
        val suggestions: List<String>,
        val searchTerms: List<String>
    )
}
