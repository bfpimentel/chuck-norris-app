package dev.pimentel.chucknorris.presentation.facts

import androidx.lifecycle.LiveData
import dev.pimentel.chucknorris.shared.abstractions.BaseContract

interface FactsContract {

    interface ViewModel : BaseContract.ViewModel {
        fun setupFacts()
        fun navigateToSearch()

        fun firstAccess(): LiveData<Unit>
        fun searchTerm(): LiveData<String>
        fun facts(): LiveData<List<FactsViewModel.FactDisplay>>
    }
}
