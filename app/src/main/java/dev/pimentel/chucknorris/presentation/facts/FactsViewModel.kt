package dev.pimentel.chucknorris.presentation.facts

import androidx.annotation.DimenRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.shared.abstractions.BaseViewModel
import dev.pimentel.chucknorris.shared.navigator.NavigatorRouter
import dev.pimentel.chucknorris.shared.schedulerprovider.SchedulerProvider
import dev.pimentel.domain.usecases.GetErrorMessage
import dev.pimentel.domain.usecases.GetFacts
import dev.pimentel.domain.usecases.shared.NoParams

class FactsViewModel(
    private val navigator: NavigatorRouter,
    private val getFacts: GetFacts,
    getErrorMessage: GetErrorMessage,
    schedulerProvider: SchedulerProvider
) : BaseViewModel(
    schedulerProvider,
    getErrorMessage
), FactsContract.ViewModel {

    private val facts = MutableLiveData<List<FactDisplay>>()

    override fun facts(): LiveData<List<FactDisplay>> = facts

    override fun initialize() {
        getFacts(NoParams)
            .compose(observeOnUIAfterSingleResult())
            .doOnSubscribe { isLoading.postValue(true) }
            .doFinally { isLoading.postValue(false) }
            .handle({
                it.map { fact ->
                    FactDisplay(
                        fact.category,
                        fact.value,
                        if (fact.value.length > SMALL_FONT_LENGTH_LIMIT) R.dimen.text_normal
                        else R.dimen.text_large
                    )
                }.also(facts::postValue)
            }, ::postErrorMessage)
    }

    override fun navigateToSearch() {
        navigator.navigate(R.id.search_fragment)
    }

    data class FactDisplay(
        val category: String,
        val value: String,
        @DimenRes val fontSize: Int
    )

    private companion object {
        const val SMALL_FONT_LENGTH_LIMIT = 80
    }
}
