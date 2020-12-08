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
            get<Navigator>(),
            FactDisplayMapperImpl(androidContext()),
            ShareableFactMapperImpl(),
            get(),
            get(),
            get(),
            ReducerImpl(FactsState.INITIAL),
            get()
        )
    }
}
