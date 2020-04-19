package dev.pimentel.chucknorris.presentation.facts

import dev.pimentel.chucknorris.shared.navigator.Navigator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val factsModule = module {
    factory { FactsAdapter() }
    viewModel { FactsViewModel(get<Navigator>(), get(), get(), get()) }
}
