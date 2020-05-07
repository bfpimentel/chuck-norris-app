package dev.pimentel.chucknorris.presentation.facts

import androidx.lifecycle.LiveData
import dev.pimentel.chucknorris.presentation.facts.mappers.ShareableFact

interface FactsContract {

    interface ViewModel {
        fun getSearchTermAndFacts()
        fun navigateToSearch()
        fun getShareableFact(id: String)

        fun factsState(): LiveData<FactsState>
        fun shareableFact(): LiveData<ShareableFact>
    }
}
