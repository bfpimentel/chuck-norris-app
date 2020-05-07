package dev.pimentel.chucknorris.presentation.search

sealed class SearchState(
    val hasSuggestions: Boolean = false,
    val categorySuggestions: List<String> = emptyList(),
    val searchTerms: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val errorMessage: String = ""
) {

    class Loading(isLoading: Boolean) : SearchState(
        isLoading = isLoading
    )

    class Success(categorySuggestions: List<String>, searchTerms: List<String>) : SearchState(
        hasSuggestions = true,
        categorySuggestions = categorySuggestions,
        searchTerms = searchTerms
    )

    class Error(errorMessage: String) : SearchState(
        hasError = true,
        errorMessage = errorMessage
    )
}
