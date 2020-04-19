package dev.pimentel.chucknorris.presentation.search

import androidx.lifecycle.LiveData
import dev.pimentel.chucknorris.shared.abstractions.BaseContract

interface SearchContract {

    interface ViewModel : BaseContract.ViewModel {
        fun initialize()
        fun saveSearchTerm(term: String)

        fun categorySuggestions(): LiveData<List<String>>
        fun searchTerms(): LiveData<List<String>>
    }
}
