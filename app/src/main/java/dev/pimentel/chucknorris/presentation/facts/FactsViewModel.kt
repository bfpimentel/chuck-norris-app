package dev.pimentel.chucknorris.presentation.facts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.pimentel.chucknorris.R
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

@Suppress("LongParameterList")
class FactsViewModel(
    private val navigator: NavigatorRouter,
    private val factDisplayMapper: FactDisplayMapper,
    private val shareableFactMapper: ShareableFactMapper,
    private val getSearchTerm: GetSearchTerm,
    private val getFacts: GetFacts,
    private val getErrorMessage: GetErrorMessage,
    schedulerProvider: SchedulerProvider
) : BaseViewModel(
    schedulerProvider,
    getErrorMessage
), FactsContract.ViewModel {

    private lateinit var facts: List<Fact>

    private val state = MutableLiveData<FactsState>()
    private val shareableFact = MutableLiveData<ShareableFact>()

    override fun factsState(): LiveData<FactsState> = state
    override fun shareableFact(): LiveData<ShareableFact> = shareableFact

    override fun navigateToSearch() {
        navigator.navigate(R.id.facts_fragment_to_search_fragment)
    }

    override fun getSearchTermAndFacts() {
        getSearchTerm(NoParams).flatMap { searchTerm ->
            getFacts(GetFacts.Params(searchTerm))
                .doOnSubscribe { state.postValue(FactsState.Loading(true)) }
                .doAfterTerminate { state.postValue(FactsState.Loading(false)) }
                .map { facts ->
                    InitializeData(
                        searchTerm,
                        facts
                    )
                }
        }.compose(observeOnUIAfterSingleResult())
            .handle({ data ->
                this.facts = data.facts

                if (facts.isEmpty()) {
                    state.postValue(FactsState.Empty(data.searchTerm))
                    return@handle
                }

                state.postValue(
                    FactsState.Success(
                        factDisplayMapper.map(facts),
                        data.searchTerm
                    )
                )
            }, { error ->
                if (error is GetSearchTerm.SearchTermNotFoundException) {
                    state.postValue(FactsState.FirstAccess())
                } else {
                    state.postValue(
                        FactsState.Error(
                            getErrorMessage(GetErrorMessage.Params(error))
                        )
                    )
                }
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
