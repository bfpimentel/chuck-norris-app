package dev.pimentel.chucknorris.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.pimentel.chucknorris.shared.abstractions.BaseViewModel
import dev.pimentel.chucknorris.shared.schedulerprovider.SchedulerProvider
import dev.pimentel.domain.entities.CategorySuggestion
import dev.pimentel.domain.usecases.GetCategorySuggestions
import dev.pimentel.domain.usecases.GetErrorMessage
import dev.pimentel.domain.usecases.shared.NoParams

class SearchViewModel(
    private val getCategorySuggestions: GetCategorySuggestions,
    getErrorMessage: GetErrorMessage,
    schedulerProvider: SchedulerProvider
) : BaseViewModel(
    schedulerProvider,
    getErrorMessage
), SearchContract.ViewModel {

    private val categorySuggestions = MutableLiveData<List<CategorySuggestion>>()

    override fun getCategorySuggestions() {
        getCategorySuggestions(NoParams)
            .compose(observeOnUIAfterSingleResult())
            .doOnSubscribe { isLoading.postValue(true) }
            .doFinally { isLoading.postValue(false) }
            .handle(categorySuggestions::postValue, ::postErrorMessage)
    }

    override fun categorySuggestions(): LiveData<List<CategorySuggestion>> = categorySuggestions
}
