package dev.pimentel.chucknorris.presentation.facts

import dev.pimentel.chucknorris.presentation.facts.data.FactsState
import dev.pimentel.chucknorris.presentation.facts.mappers.FactDisplayMapperImpl
import dev.pimentel.chucknorris.presentation.facts.mappers.ShareableFactMapperImpl
import dev.pimentel.chucknorris.shared.mvi.ReducerImpl
import dev.pimentel.chucknorris.shared.navigator.Navigator
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val factsModule = module {
    factory { FactsAdapter() }
    viewModel {
        FactsViewModel(
            navigator = get<Navigator>(),
            factDisplayMapper = FactDisplayMapperImpl(androidContext()),
            shareableFactMapper = ShareableFactMapperImpl(),
            getSearchTerm = get(),
            getFacts = get(),
            getErrorMessage = get(),
            reducer = ReducerImpl(FactsState.INITIAL),
            dispatchersProvider = get()
        )
    }
}
