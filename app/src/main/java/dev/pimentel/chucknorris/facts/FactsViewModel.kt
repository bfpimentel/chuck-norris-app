package dev.pimentel.chucknorris.facts

import dev.pimentel.chucknorris.shared.abstractions.BaseViewModel
import dev.pimentel.chucknorris.shared.schedulerprovider.SchedulerProvider
import dev.pimentel.domain.usecases.GetErrorType

class FactsViewModel(
    getErrorType: GetErrorType,
    schedulerProvider: SchedulerProvider
) : BaseViewModel(
    schedulerProvider,
    getErrorType
), FactsContract.ViewModel
