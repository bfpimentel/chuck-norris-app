package dev.pimentel.chucknorris.presentation.search

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    factory { SearchTermsAdapter() }
    viewModel { SearchViewModel(get(), get(), get(), get(), get()) }
}
