package dev.pimentel.chucknorris.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.pimentel.chucknorris.shared.abstractions.BaseViewModel
import dev.pimentel.chucknorris.shared.schedulerprovider.SchedulerProvider
import dev.pimentel.domain.entities.CategorySuggestion
import dev.pimentel.domain.usecases.GetCategorySuggestions
import dev.pimentel.domain.usecases.GetErrorMessage
import dev.pimentel.domain.usecases.HandleSearchTermSaving
import dev.pimentel.domain.usecases.shared.NoParams

class SearchViewModel(
    private val getCategorySuggestions: GetCategorySuggestions,
    private val handleSearchTermSaving: HandleSearchTermSaving,
    getErrorMessage: GetErrorMessage,
    schedulerProvider: SchedulerProvider
) : BaseViewModel(
    schedulerProvider,
    getErrorMessage
), SearchContract.ViewModel {

    private val categorySuggestions = MutableLiveData<List<CategorySuggestion>>()
    private val searchTermSuccess = MutableLiveData<Unit>()

    override fun getCategorySuggestions() {
        getCategorySuggestions(NoParams)
            .compose(observeOnUIAfterSingleResult())
            .doOnSubscribe { isLoading.postValue(true) }
            .doFinally { isLoading.postValue(false) }
            .handle(categorySuggestions::postValue, ::postErrorMessage)
    }

    override fun saveSearchTerm(term: String) {
        handleSearchTermSaving(HandleSearchTermSaving.Params(term))
            .compose(observeOnUIAfterCompletableResult())
            .handle({ searchTermSuccess.postValue(Unit) }, ::postErrorMessage)
    }

    override fun categorySuggestions(): LiveData<List<CategorySuggestion>> = categorySuggestions

    override fun searchTermSuccess(): LiveData<Unit> = searchTermSuccess
}
