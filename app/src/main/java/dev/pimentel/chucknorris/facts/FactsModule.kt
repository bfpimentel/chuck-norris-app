package dev.pimentel.chucknorris.facts

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val viewModelModule = module {
    viewModel { FactsViewModel(get(), get()) }
}

val factsModule = listOf(
    viewModelModule
)
