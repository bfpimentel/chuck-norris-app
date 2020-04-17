package dev.pimentel.chucknorris.presentation.search

import dev.pimentel.chucknorris.shared.abstractions.BaseViewModel
import dev.pimentel.chucknorris.shared.schedulerprovider.SchedulerProvider
import dev.pimentel.domain.usecases.GetErrorType

class SearchViewModel(
    getErrorType: GetErrorType,
    schedulerProvider: SchedulerProvider
) : BaseViewModel(
    schedulerProvider,
    getErrorType
), SearchContract.ViewModel
