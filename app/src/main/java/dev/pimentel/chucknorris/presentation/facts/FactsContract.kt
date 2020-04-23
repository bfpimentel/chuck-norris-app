package dev.pimentel.chucknorris.presentation.facts

import androidx.lifecycle.LiveData
import dev.pimentel.chucknorris.shared.abstractions.BaseContract

interface FactsContract {

    interface ViewModel : BaseContract.ViewModel {
        fun setupFacts()
        fun navigateToSearch()
        fun getShareableFact(id: String)

        fun firstAccess(): LiveData<Unit>
        fun searchTerm(): LiveData<String>
        fun facts(): LiveData<List<FactsViewModel.FactDisplay>>
        fun shareableFact(): LiveData<FactsViewModel.ShareableFact>
        fun listIsEmpty(): LiveData<Unit>
    }
}
