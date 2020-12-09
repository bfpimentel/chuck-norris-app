package dev.pimentel.chucknorris.presentation.facts.data

import dev.pimentel.chucknorris.presentation.facts.mappers.FactDisplay
import dev.pimentel.chucknorris.presentation.facts.mappers.ShareableFact
import dev.pimentel.chucknorris.shared.mvi.Event

@Suppress("LongParameterList")
data class FactsState(
    val facts: List<FactDisplay>,
    val searchTerm: String,
    val isFirstAccess: Boolean,
    val isLoading: Boolean,
    val emptyListEvent: Event.NoContent?,
    val shareFactEvent: Event<ShareableFact>?,
    val errorEvent: Event<String>?,
) {

    companion object {
        @JvmStatic
        val INITIAL = FactsState(
            facts = emptyList(),
            searchTerm = "",
            isFirstAccess = false,
            isLoading = false,
            emptyListEvent = null,
            shareFactEvent = null,
            errorEvent = null
        )
    }
}

//@Suppress("LongParameterList")
//sealed class FactsState(
//    val facts: List<FactDisplay> = emptyList(),
//    val hasFacts: Boolean = false,
//    val searchTerm: String = "",
//    val isFirstAccess: Boolean = false,
//    val isLoading: Boolean = false,
//    val isEmpty: Boolean = false,
//    val hasError: Boolean = false,
//    val errorMessage: String = "",
//    val shareFactEvent: Event<ShareableFact>? = null
//) {
//
//    object Initial : FactsState()
//
//    object FirstAccess : FactsState(
//        isFirstAccess = true
//    )
//
//    class Loading(isLoading: Boolean) : FactsState(
//        isLoading = isLoading
//    )
//
//    class Empty(searchTerm: String) : FactsState(
//        isEmpty = true,
//        searchTerm = searchTerm
//    )
//
//    class Success(facts: List<FactDisplay>, searchTerm: String) : FactsState(
//        facts = facts,
//        searchTerm = searchTerm,
//        hasFacts = true
//    )
//
//    class Share(shareableFact: ShareableFact) : FactsState(
//        shareFactEvent = shareableFact.toEvent()
//    )
//
//    class Error(errorMessage: String) : FactsState(
//        hasError = true,
//        errorMessage = errorMessage
//    )
//}
