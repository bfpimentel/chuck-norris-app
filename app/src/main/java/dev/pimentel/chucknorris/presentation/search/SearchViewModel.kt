package dev.pimentel.chucknorris.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.pimentel.chucknorris.shared.abstractions.BaseViewModel
import dev.pimentel.chucknorris.shared.schedulerprovider.SchedulerProvider
import dev.pimentel.domain.entities.CategorySuggestion
import dev.pimentel.domain.usecases.GetCategorySuggestions
import dev.pimentel.domain.usecases.GetErrorMessage
import dev.pimentel.domain.usecases.GetLastSearchTerms
import dev.pimentel.domain.usecases.HandleSearchTermSaving
import dev.pimentel.domain.usecases.shared.NoParams

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

    private val categorySuggestions = MutableLiveData<List<CategorySuggestion>>()
    private val searchTerms = MutableLiveData<List<String>>()

    override fun categorySuggestions(): LiveData<List<CategorySuggestion>> = categorySuggestions

    override fun searchTerms(): LiveData<List<String>> = searchTerms

    override fun getCategorySuggestions() {
        getCategorySuggestions(NoParams)
            .compose(observeOnUIAfterSingleResult())
            .doOnSubscribe { isLoading.postValue(true) }
            .doFinally { isLoading.postValue(false) }
            .handle({ suggestions ->
                categorySuggestions.postValue(suggestions)
                getLastSearchTerms()
            }, ::postErrorMessage)
    }

    override fun saveSearchTerm(term: String) {
        handleSearchTermSaving(HandleSearchTermSaving.Params(term))
            .compose(observeOnUIAfterCompletableResult())
            .handle({ }, ::postErrorMessage)
    }

    private fun getLastSearchTerms() {
        getLastSearchTerms(NoParams)
            .compose(observeOnUIAfterSingleResult())
            .handle(searchTerms::postValue, ::postErrorMessage)
    }
}
