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

    class WithFacts(factsEvent: Event<List<FactViewData>>, searchTerm: String) : FactsState(
        factsEvent = factsEvent,
        searchTerm = searchTerm,
        hasFacts = true,
    )

    class Share(shareableFact: ShareableFact) : FactsState(
        shareFactEvent = shareableFact.toEvent(),
    )

    class Error(errorEvent: Event<String>) : FactsState(
        hasError = true,
        errorEvent = errorEvent,
    )
}
