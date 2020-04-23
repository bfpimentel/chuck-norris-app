package dev.pimentel.chucknorris.presentation.search

import androidx.lifecycle.LiveData
import dev.pimentel.chucknorris.shared.abstractions.BaseContract

interface SearchContract {

    interface ViewModel : BaseContract.ViewModel {
        fun getCategorySuggestionsAndSearchTerms()
        fun saveSearchTerm(term: String)

        fun selectedSuggestionIndex(): LiveData<Int>
        fun categorySuggestions(): LiveData<List<String>>
        fun searchTerms(): LiveData<List<String>>
    }
}
