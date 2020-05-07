package dev.pimentel.chucknorris.presentation.facts

import androidx.lifecycle.LiveData
import dev.pimentel.chucknorris.presentation.facts.mappers.ShareableFact
import dev.pimentel.chucknorris.shared.abstractions.BaseContract

interface FactsContract {

    interface ViewModel : BaseContract.ViewModel {
        fun getSearchTermAndFacts()
        fun navigateToSearch()
        fun getShareableFact(id: String)

        fun factsState(): LiveData<FactsState>
        fun shareableFact(): LiveData<ShareableFact>
    }
}
