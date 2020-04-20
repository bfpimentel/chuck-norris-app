package dev.pimentel.chucknorris.presentation.search

import dev.pimentel.chucknorris.shared.navigator.Navigator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    factory { SearchTermsAdapter() }
    viewModel { SearchViewModel(get<Navigator>(), get(), get(), get(), get(), get()) }
}
