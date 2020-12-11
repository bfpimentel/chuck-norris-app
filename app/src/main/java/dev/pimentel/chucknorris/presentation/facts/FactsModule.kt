package dev.pimentel.chucknorris.presentation.facts

import dev.pimentel.chucknorris.presentation.facts.mappers.FactViewDataMapperImpl
import dev.pimentel.chucknorris.presentation.facts.mappers.ShareableFactMapperImpl
import dev.pimentel.chucknorris.shared.navigator.Navigator
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val factsModule = module {
    factory { FactsAdapter() }
    viewModel {
        FactsViewModel(
            navigator = get<Navigator>(),
            factDisplayMapper = FactViewDataMapperImpl(androidContext()),
            shareableFactMapper = ShareableFactMapperImpl(),
            getSearchTerm = get(),
            getFacts = get(),
            getErrorMessage = get(),
            dispatchersProvider = get()
        )
    }
}
