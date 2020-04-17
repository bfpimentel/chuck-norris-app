package dev.pimentel.chucknorris.presentation.search

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    viewModel { SearchViewModel(get(), get(), get()) }
}
