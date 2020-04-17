package dev.pimentel.chucknorris.presentation.facts

import dev.pimentel.chucknorris.shared.abstractions.BaseContract

interface FactsContract {

    interface ViewModel : BaseContract.ViewModel {
        fun navigateToSearch()
    }
}
