package dev.pimentel.chucknorris.presentation.search

import androidx.lifecycle.LiveData
import dev.pimentel.chucknorris.shared.abstractions.BaseContract
import dev.pimentel.domain.entities.CategorySuggestion

interface SearchContract {

    interface ViewModel : BaseContract.ViewModel {
        fun getCategorySuggestions()

        fun categorySuggestions(): LiveData<List<CategorySuggestion>>
    }
}
