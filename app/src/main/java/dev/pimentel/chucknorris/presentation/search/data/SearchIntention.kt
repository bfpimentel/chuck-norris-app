package dev.pimentel.chucknorris.presentation.search.data

sealed class SearchIntention {

    object GetCategorySuggestionsAndSearchTerms : SearchIntention()

    data class SaveSearchTerm(val term: String) : SearchIntention()
}