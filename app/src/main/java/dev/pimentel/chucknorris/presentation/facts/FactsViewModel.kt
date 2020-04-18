package dev.pimentel.chucknorris.presentation.facts

import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.shared.abstractions.BaseViewModel
import dev.pimentel.chucknorris.shared.navigator.NavigatorRouter
import dev.pimentel.chucknorris.shared.schedulerprovider.SchedulerProvider
import dev.pimentel.domain.usecases.GetErrorMessage

class FactsViewModel(
    private val navigator: NavigatorRouter,
    getErrorMessage: GetErrorMessage,
    schedulerProvider: SchedulerProvider
) : BaseViewModel(
    schedulerProvider,
    getErrorMessage
), FactsContract.ViewModel {

    override fun navigateToSearch() {
        navigator.navigate(R.id.search_fragment)
    }
}
