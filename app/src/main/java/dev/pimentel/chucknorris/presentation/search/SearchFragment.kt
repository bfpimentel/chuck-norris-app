package dev.pimentel.chucknorris.presentation.search

import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.databinding.SearchFragmentLayoutBinding
import dev.pimentel.chucknorris.shared.abstractions.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.module.Module

class SearchFragment : BaseFragment<SearchContract.ViewModel, SearchFragmentLayoutBinding>(
    R.layout.search_fragment_layout
) {

    override val module: Module = searchModule
    override val viewModel: SearchContract.ViewModel by viewModel<SearchViewModel>()

    override fun bindView() = initBinding(
        SearchFragmentLayoutBinding.inflate(layoutInflater),
        this
    ) { }
}
