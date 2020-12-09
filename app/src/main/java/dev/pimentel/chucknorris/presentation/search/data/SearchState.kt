package dev.pimentel.chucknorris.presentation.search.data

import dev.pimentel.chucknorris.shared.mvi.Event

data class SearchState(
    val categorySuggestions: List<String>,
    val searchTerms: List<String>,
    val isLoading: Boolean,
    val selectSuggestionEvent: Event<Int>?,
    val newSearch: Event<String>?,
    val errorEvent: Event<String>?,
) {

    companion object {
        @JvmStatic
        val INITIAL = SearchState(
            categorySuggestions = emptyList(),
            searchTerms = emptyList(),
            isLoading = false,
            selectSuggestionEvent = null,
            newSearch = null,
            errorEvent = null,
        )
    }
}
