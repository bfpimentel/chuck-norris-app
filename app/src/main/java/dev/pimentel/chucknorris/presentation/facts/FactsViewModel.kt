package dev.pimentel.chucknorris.presentation.facts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.presentation.facts.mappers.FactDisplay
import dev.pimentel.chucknorris.presentation.facts.mappers.FactDisplayMapper
import dev.pimentel.chucknorris.presentation.facts.mappers.ShareableFact
import dev.pimentel.chucknorris.presentation.facts.mappers.ShareableFactMapper
import dev.pimentel.chucknorris.shared.abstractions.BaseViewModel
import dev.pimentel.chucknorris.shared.errorhandling.GetErrorMessage
import dev.pimentel.chucknorris.shared.navigator.NavigatorRouter
import dev.pimentel.chucknorris.shared.schedulerprovider.SchedulerProvider
import dev.pimentel.domain.entities.Fact
import dev.pimentel.domain.usecases.GetFacts
import dev.pimentel.domain.usecases.GetSearchTerm
import dev.pimentel.domain.usecases.shared.NoParams

class FactsViewModel(
    private val navigator: NavigatorRouter,
    private val factDisplayMapper: FactDisplayMapper,
    private val shareableFactMapper: ShareableFactMapper,
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

    override fun getSearchTermAndFacts() {
        getSearchTerm(NoParams).flatMap { searchTerm ->
            getFacts(GetFacts.Params(searchTerm))
                .doOnSubscribe { isLoading.postValue(Unit) }
                .doAfterTerminate { isNotLoading.postValue(Unit) }
                .map { facts ->
                    InitializeData(
                        searchTerm,
                        facts
                    )
                }
        }.compose(observeOnUIAfterSingleResult())
            .handle({ data ->
                searchTerm.postValue(data.searchTerm)

                this.facts = data.facts

                if (facts.isEmpty()) {
                    listIsEmpty.postValue(Unit)
                    return@handle
                }

                factDisplayMapper.map(facts)
                    .also(factsDisplays::postValue)
            }, { error ->
                if (error is GetSearchTerm.SearchTermNotFoundException) firstAccess.postValue(Unit)
                else postErrorMessage(error)
            })
    }

    override fun getShareableFact(id: String) {
        facts.first { it.id == id }
            .let(shareableFactMapper::map)
            .also(shareableFact::postValue)
    }

    private data class InitializeData(
        val searchTerm: String,
        val facts: List<Fact>
    )
}
