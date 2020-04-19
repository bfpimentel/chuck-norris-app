package dev.pimentel.chucknorris.presentation.facts

import androidx.annotation.DimenRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.shared.abstractions.BaseViewModel
import dev.pimentel.chucknorris.shared.navigator.NavigatorRouter
import dev.pimentel.chucknorris.shared.schedulerprovider.SchedulerProvider
import dev.pimentel.domain.entities.Fact
import dev.pimentel.domain.usecases.GetErrorMessage
import dev.pimentel.domain.usecases.GetFacts
import dev.pimentel.domain.usecases.GetSearchTerm
import dev.pimentel.domain.usecases.shared.NoParams

class FactsViewModel(
    private val navigator: NavigatorRouter,
    private val getSearchTerm: GetSearchTerm,
    private val getFacts: GetFacts,
    getErrorMessage: GetErrorMessage,
    schedulerProvider: SchedulerProvider
) : BaseViewModel(
    schedulerProvider,
    getErrorMessage
), FactsContract.ViewModel {

    private val firstAccess = MutableLiveData<Unit>()
    private val searchTerm = MutableLiveData<String>()
    private val facts = MutableLiveData<List<FactDisplay>>()

    override fun firstAccess(): LiveData<Unit> = firstAccess

    override fun searchTerm(): LiveData<String> = searchTerm

    override fun facts(): LiveData<List<FactDisplay>> = facts

    override fun setupFacts() {
        getSearchTerm(NoParams).flatMap { searchTerm ->
            getFacts(GetFacts.Params(searchTerm)).map { facts ->
                InitializeData(
                    searchTerm,
                    facts
                )
            }
        }.compose(observeOnUIAfterSingleResult())
            .doOnSubscribe { isLoading.postValue(true) }
            .doFinally { isLoading.postValue(false) }
            .handle({ data ->
                searchTerm.postValue(data.searchTerm)

                data.facts.map { fact ->
                    FactDisplay(
                        fact.category,
                        fact.value,
                        if (fact.value.length > SMALL_FONT_LENGTH_LIMIT) R.dimen.text_normal
                        else R.dimen.text_large
                    )
                }.also(facts::postValue)
            }, { error ->
                if (error is GetSearchTerm.SearchTermNotFoundException) {
                    firstAccess.postValue(Unit)
                } else {
                    postErrorMessage(error)
                }
            })
    }

    override fun navigateToSearch() {
        navigator.navigate(R.id.search_fragment)
    }

    data class FactDisplay(
        val category: String,
        val value: String,
        @DimenRes val fontSize: Int
    )

    private data class InitializeData(
        val searchTerm: String,
        val facts: List<Fact>
    )

    private companion object {
        const val SMALL_FONT_LENGTH_LIMIT = 80
    }
}
