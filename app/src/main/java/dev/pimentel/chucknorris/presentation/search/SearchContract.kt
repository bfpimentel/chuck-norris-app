package dev.pimentel.chucknorris.presentation.search

import androidx.lifecycle.LiveData

interface SearchContract {

    interface ViewModel {
        fun getCategorySuggestionsAndSearchTerms()
        fun saveSearchTerm(term: String)

        fun searchState(): LiveData<SearchState>
        fun selectedSuggestionIndex(): LiveData<Int>
    }
}
