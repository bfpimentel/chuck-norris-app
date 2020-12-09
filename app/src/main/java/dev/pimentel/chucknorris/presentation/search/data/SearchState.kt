package dev.pimentel.chucknorris.presentation.search.data

import dev.pimentel.chucknorris.shared.mvi.Event

data class SearchState(
    val categorySuggestions: List<String>,
    val searchTerms: List<String>,
    val isLoading: Boolean,
    val errorEvent: Event<String>?,
    val selectSuggestionEvent: Event<Int>?
) {

    companion object {
        @JvmStatic
        val INITIAL = SearchState(
            categorySuggestions = emptyList(),
            searchTerms = emptyList(),
            isLoading = false,
            errorEvent = null,
            selectSuggestionEvent = null,
        )
    }
}
