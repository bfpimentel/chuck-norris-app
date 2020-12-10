package dev.pimentel.chucknorris.presentation.facts.data

import dev.pimentel.chucknorris.presentation.facts.mappers.FactDisplay
import dev.pimentel.chucknorris.presentation.facts.mappers.ShareableFact
import dev.pimentel.chucknorris.shared.mvi.Event
import dev.pimentel.chucknorris.shared.mvi.toEvent

//@Suppress("LongParameterList")
//data class FactsState(
//    val facts: List<FactDisplay>,
//    val searchTerm: String,
//    val isFirstAccess: Boolean,
//    val isLoading: Boolean,
//    val hasError: Boolean,
//    val isEmpty: Boolean,
//    val shareFactEvent: Event<ShareableFact>?,
//    val errorMessageEvent: Event<String>?,
//) {
//
//    companion object {
//        @JvmStatic
//        val INITIAL = FactsState(
//            facts = emptyList(),
//            searchTerm = "",
//            isFirstAccess = false,
//            isLoading = false,
//            isEmpty = false,
//            hasError = false,
//            shareFactEvent = null,
//            errorMessageEvent = null
//        )
//    }
//}

@Suppress("LongParameterList")
sealed class FactsState(
    val factsEvent: Event<List<FactDisplay>>? = null,
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

    class WithFacts(factsEvent: Event<List<FactDisplay>>, searchTerm: String) : FactsState(
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
