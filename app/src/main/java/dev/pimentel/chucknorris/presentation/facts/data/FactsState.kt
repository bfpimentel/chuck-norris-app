package dev.pimentel.chucknorris.presentation.facts.data

import dev.pimentel.chucknorris.shared.mvi.Event
import dev.pimentel.chucknorris.shared.mvi.toEvent

@Suppress("LongParameterList")
sealed class FactsState(
    val factsEvent: Event<List<FactViewData>>? = null,
    val searchTerm: String = "",
    val isFirstAccess: Boolean = false,
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val hasFacts: Boolean = false,
    val hasError: Boolean = false,
    val errorEvent: Event<String>? = null,
    val shareFactEvent: Event<ShareableFact>? = null,
) {

    object Initial : FactsState()

    object FirstAccess : FactsState(
        isFirstAccess = true
    )

    class Loading(isLoading: Boolean) : FactsState(
        isLoading = isLoading
    )

    class Empty(searchTerm: String) : FactsState(
        isEmpty = true,
        searchTerm = searchTerm,
    )

    class WithFacts(facts: List<FactViewData>, searchTerm: String) : FactsState(
        factsEvent = facts.toEvent(),
        searchTerm = searchTerm,
        hasFacts = true,
    )

    class Share(oldState: FactsState, shareableFact: ShareableFact) : FactsState(
        factsEvent = oldState.factsEvent,
        searchTerm = oldState.searchTerm,
        isFirstAccess = oldState.isFirstAccess,
        isLoading = oldState.isLoading,
        isEmpty = oldState.isEmpty,
        hasFacts = oldState.hasFacts,
        hasError = oldState.hasError,
        errorEvent = oldState.errorEvent,
        shareFactEvent = shareableFact.toEvent(),
    )

    class Error(errorMessage: String) : FactsState(
        hasError = true,
        errorEvent = errorMessage.toEvent(),
    )
}
