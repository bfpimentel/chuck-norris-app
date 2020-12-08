package dev.pimentel.chucknorris.presentation.facts.data

import dev.pimentel.chucknorris.presentation.facts.mappers.FactDisplay
import dev.pimentel.chucknorris.presentation.facts.mappers.ShareableFact
import dev.pimentel.chucknorris.shared.mvi.Event

@Suppress("LongParameterList")
data class FactsState(
    val facts: List<FactDisplay>,
    val hasFacts: Boolean,
    val searchTerm: String,
    val isFirstAccess: Boolean,
    val isLoading: Boolean,
    val isEmpty: Boolean,
    val hasError: Boolean,
    val shareFactEvent: Event<ShareableFact>?,
    val errorEvent: Event<String>?,
) {

//    class FirstAccess : FactsState(
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
//    class Success(facts: List<FactDisplay>, searchTerm: String) :
//        FactsState(
//            facts = facts,
//            searchTerm = searchTerm,
//            hasFacts = true
//        )
//
//    class Error(errorMessage: String) : FactsState(
//        hasError = true,
//        errorMessage = errorMessage
//    )

    companion object {
        @JvmStatic
        val INITIAL = FactsState(
            facts = emptyList(),
            hasFacts = false,
            searchTerm = "",
            isFirstAccess = false,
            isLoading = false,
            isEmpty = false,
            hasError = false,
            shareFactEvent = null,
            errorEvent = null
        )
    }
}
