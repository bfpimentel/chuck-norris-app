package dev.pimentel.chucknorris.presentation.facts

import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.databinding.FactsFragmentLayoutBinding
import dev.pimentel.chucknorris.shared.abstractions.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FactsFragment : BaseFragment<FactsContract.ViewModel, FactsFragmentLayoutBinding>(
    R.layout.facts_fragment_layout
) {

    override val modules = factsModule
    override val viewModel: FactsContract.ViewModel by viewModel<FactsViewModel>()

    override fun bindView() = initBinding(
        FactsFragmentLayoutBinding.inflate(layoutInflater),
        this
    ) { }
}
