package dev.pimentel.chucknorris.presentation.search.data

import dev.pimentel.chucknorris.shared.mvi.Event

sealed class SearchState(
    val categorySuggestions: List<String> = emptyList(),
    val searchTermsEvent: Event<List<String>>? = null,
    val isLoading: Boolean = false,
    val hasSuggestions: Boolean = false,
    val selectSuggestionEvent: Event<Int>? = null,
    val newSearch: Event<String>? = null,
    val hasError: Boolean = false,
    val errorEvent: Event<String>? = null,
) {

    object Initial : SearchState()

    class Loading(isLoading: Boolean) : SearchState(
        isLoading = isLoading
    )

    class Success(
        categorySuggestions: List<String>,
        searchTerms: Event<List<String>>,
        selectSuggestionEvent: Event<Int>?,
    ) : SearchState(
        hasSuggestions = true,
        categorySuggestions = categorySuggestions,
        searchTermsEvent = searchTerms,
        selectSuggestionEvent = selectSuggestionEvent
    )

    class NewSearch(newSearch: Event<String>) : SearchState(
        newSearch = newSearch
    )

    class Error(errorMessage: Event<String>) : SearchState(
        hasError = true,
        errorEvent = errorMessage
    )
}
