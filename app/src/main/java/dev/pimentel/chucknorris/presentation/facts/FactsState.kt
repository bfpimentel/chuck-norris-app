package dev.pimentel.chucknorris.presentation.facts

import dev.pimentel.chucknorris.presentation.facts.mappers.FactDisplay

sealed class FactsState(
    val facts: List<FactDisplay> = emptyList(),
    val hasFacts: Boolean = false,
    val searchTerm: String = "",
    val isFirstAccess: Boolean = false,
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val hasError: Boolean = false,
    val errorMessage: String = ""
) {

    class FirstAccess : FactsState(
        isFirstAccess = true
    )

    class Loading(isLoading: Boolean) : FactsState(
        isLoading = isLoading
    )

    class Empty(searchTerm: String) : FactsState(
        isEmpty = true,
        searchTerm = searchTerm
    )

    class Success(facts: List<FactDisplay>, searchTerm: String) :
        FactsState(
            facts = facts,
            searchTerm = searchTerm,
            hasFacts = true
        )

    class Error(errorMessage: String) : FactsState(
        hasError = true,
        errorMessage = errorMessage
    )
}
