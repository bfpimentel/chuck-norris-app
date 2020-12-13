package dev.pimentel.chucknorris.presentation.search.data

import dev.pimentel.chucknorris.shared.mvi.Event
import dev.pimentel.chucknorris.shared.mvi.toEvent

@Suppress("LongParameterList")
sealed class SearchState(
    val suggestionsEvent: Event<List<String>>? = null,
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
        searchTerms: List<String>,
        selectedSuggestionIndex: Int?,
    ) : SearchState(
        hasSuggestions = true,
        suggestionsEvent = categorySuggestions.toEvent(),
        searchTermsEvent = searchTerms.toEvent(),
        selectSuggestionEvent = selectedSuggestionIndex?.toEvent()
    )

    class NewSearch(newSearchTerm: String) : SearchState(
        newSearch = newSearchTerm.toEvent()
    )

    class Error(errorMessage: String) : SearchState(
        hasError = true,
        errorEvent = errorMessage.toEvent()
    )
}
