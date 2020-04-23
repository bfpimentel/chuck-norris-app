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
import java.util.concurrent.TimeUnit

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

    private lateinit var facts: List<Fact>

    private val firstAccess = MutableLiveData<Unit>()
    private val searchTerm = MutableLiveData<String>()
    private val factsDisplays = MutableLiveData<List<FactDisplay>>()
    private val shareableFact = MutableLiveData<ShareableFact>()
    private val listIsEmpty = MutableLiveData<Unit>()

    override fun firstAccess(): LiveData<Unit> = firstAccess
    override fun searchTerm(): LiveData<String> = searchTerm
    override fun facts(): LiveData<List<FactDisplay>> = factsDisplays
    override fun shareableFact(): LiveData<ShareableFact> = shareableFact
    override fun listIsEmpty(): LiveData<Unit> = listIsEmpty

    override fun navigateToSearch() {
        navigator.navigate(R.id.facts_fragment_to_search_fragment)
    }

    override fun getShareableFact(id: String) {
        facts.first { it.id == id }
            .let {
                ShareableFact(
                    it.url,
                    it.value
                )
            }.also(shareableFact::postValue)
    }

    override fun setupFacts() {
        getSearchTerm(NoParams)
            .compose(observeOnUIAfterSingleResult())
            .handle({ term ->
                searchTerm.postValue(term)
                getFacts(term)
            }, { firstAccess.postValue(Unit) })
    }

    private fun getFacts(searchTerm: String) {
        getFacts(GetFacts.Params(searchTerm))
            .compose(observeOnUIAfterSingleResult())
            .doOnSubscribe { isLoading.postValue(Unit) }
            .doAfterTerminate { isNotLoading.postValue(Unit) }
            .handle(
                { facts ->
                    this.facts = facts

                    if (facts.isEmpty()) {
                        listIsEmpty.postValue(Unit)
                        return@handle
                    }

                    facts.map { fact ->
                        FactDisplay(
                            fact.id,
                            fact.category.capitalize(),
                            fact.value,
                            if (fact.value.length > SMALL_FONT_LENGTH_LIMIT) R.dimen.text_normal
                            else R.dimen.text_large
                        )
                    }.also(factsDisplays::postValue)
                }, ::postErrorMessage
            )
    }

    data class FactDisplay(
        val id: String,
        val category: String,
        val value: String,
        @DimenRes val fontSize: Int
    )

    data class ShareableFact(
        val url: String,
        val value: String
    )

    private companion object {
        const val SMALL_FONT_LENGTH_LIMIT = 80
    }
}
