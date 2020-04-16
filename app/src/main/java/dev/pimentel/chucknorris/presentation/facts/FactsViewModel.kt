package dev.pimentel.chucknorris.presentation.facts

import dev.pimentel.chucknorris.testshared.abstractions.BaseViewModel
import dev.pimentel.chucknorris.testshared.schedulerprovider.SchedulerProvider
import dev.pimentel.domain.usecases.GetErrorType

class FactsViewModel(
    getErrorType: GetErrorType,
    schedulerProvider: SchedulerProvider
) : BaseViewModel(
    schedulerProvider,
    getErrorType
), FactsContract.ViewModel
