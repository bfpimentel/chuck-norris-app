package dev.pimentel.chucknorris.presentation.facts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.presentation.facts.mappers.FactDisplayMapper
import dev.pimentel.chucknorris.presentation.facts.mappers.ShareableFact
import dev.pimentel.chucknorris.presentation.facts.mappers.ShareableFactMapper
import dev.pimentel.chucknorris.shared.errorhandling.GetErrorMessage
import dev.pimentel.chucknorris.shared.helpers.DisposablesHolder
import dev.pimentel.chucknorris.shared.helpers.DisposablesHolderImpl
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
) : ViewModel(),
    DisposablesHolder by DisposablesHolderImpl(schedulerProvider),
    FactsContract.ViewModel {

    private lateinit var facts: List<Fact>

    private val factsState = MutableLiveData<FactsState>()
    private val shareableFact = MutableLiveData<ShareableFact>()

    override fun factsState(): LiveData<FactsState> = factsState
    override fun shareableFact(): LiveData<ShareableFact> = shareableFact

    override fun onCleared() {
        super.onCleared()
        dispose()
    }

    override fun navigateToSearch() {
        navigator.navigate(R.id.facts_fragment_to_search_fragment)
    }

    override fun getSearchTermAndFacts() {
        getSearchTerm(NoParams).flatMap { searchTerm ->
            getFacts(GetFacts.Params(searchTerm))
                .doOnSubscribe { factsState.postValue(FactsState.Loading(true)) }
                .doAfterTerminate { factsState.postValue(FactsState.Loading(false)) }
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
                    factsState.postValue(FactsState.Empty(data.searchTerm))
                    return@handle
                }

                factsState.postValue(
                    FactsState.Success(
                        factDisplayMapper.map(facts),
                        data.searchTerm
                    )
                )
            }, { error ->
                if (error is GetSearchTerm.SearchTermNotFoundException) {
                    factsState.postValue(FactsState.FirstAccess())
                } else {
                    factsState.postValue(
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
