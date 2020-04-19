package dev.pimentel.chucknorris.presentation.facts

import androidx.lifecycle.LiveData
import dev.pimentel.chucknorris.shared.abstractions.BaseContract

interface FactsContract {

    interface ViewModel : BaseContract.ViewModel {
        fun initialize()
        fun navigateToSearch()

        fun facts(): LiveData<List<FactsViewModel.FactDisplay>>
    }
}
