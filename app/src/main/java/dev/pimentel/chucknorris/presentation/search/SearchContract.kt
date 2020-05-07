package dev.pimentel.chucknorris.presentation.search

import androidx.lifecycle.LiveData
import dev.pimentel.chucknorris.shared.abstractions.BaseContract

interface SearchContract {

    interface ViewModel : BaseContract.ViewModel {
        fun getCategorySuggestionsAndSearchTerms()
        fun saveSearchTerm(term: String)

        fun searchState(): LiveData<SearchState>
        fun selectedSuggestionIndex(): LiveData<Int>
    }
}
