package dev.pimentel.chucknorris.presentation.facts

import androidx.recyclerview.widget.LinearLayoutManager
import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.databinding.FactsLayoutBinding
import dev.pimentel.chucknorris.shared.abstractions.BaseFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FactsFragment : BaseFragment<FactsContract.ViewModel, FactsLayoutBinding>(
    R.layout.facts_layout
) {

    override val module = factsModule
    override val viewModel: FactsContract.ViewModel by viewModel<FactsViewModel>()
    private val adapter: FactsAdapter by inject()

    override fun bindView() = initBinding(
        FactsLayoutBinding.inflate(layoutInflater),
        this
    ) {
        factsRvFacts.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.searchTerm().observe { searchTerm ->
            factsTvSearchTerm.text = searchTerm
        }

        viewModel.facts().observe { facts ->
            adapter.submitList(facts)
        }

        factsMbGoToSearch.setOnClickListener {
            viewModel.navigateToSearch()
        }

        viewModel.initialize()
    }
}
