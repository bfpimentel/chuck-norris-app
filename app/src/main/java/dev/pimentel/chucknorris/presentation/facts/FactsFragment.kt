package dev.pimentel.chucknorris.presentation.facts

import androidx.core.app.ShareCompat
import androidx.core.view.isVisible
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
            it.adapter = adapter.apply {
                onItemClick = viewModel::getShareableFact
            }
            it.layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.firstAccess().observe {
            factsAblSearchTerm.isVisible = false
            factsRvFacts.isVisible = false
            factsTvFirstAccess.isVisible = true
            factsTvError.isVisible = false
        }

        viewModel.searchTerm().observe { searchTerm ->
            factsTvSearchTerm.text = searchTerm
            factsAblSearchTerm.isVisible = true
        }

        viewModel.facts().observe { facts ->
            adapter.submitList(facts)
            factsRvFacts.isVisible = true
            factsTvFirstAccess.isVisible = false
            factsTvError.isVisible = false
        }

        viewModel.error().observe { errorMessage ->
            factsTvError.isVisible = true
            factsAblSearchTerm.isVisible = false
            factsRvFacts.isVisible = false
            factsTvError.text = getString(R.string.facts_tv_error_message, errorMessage)
        }

        viewModel.shareableFact().observe(::shareFact)

        factsMbGoToSearch.setOnClickListener {
            viewModel.navigateToSearch()
        }

        factsTvError.setOnClickListener {
            viewModel.setupFacts()
        }

        viewModel.setupFacts()
    }

    private fun shareFact(shareableFact: FactsViewModel.ShareableFact) {
        ShareCompat.IntentBuilder
            .from(requireActivity())
            .setType(SHARE_TYPE)
            .setText(
                getString(
                    R.string.facts_shareable_message,
                    shareableFact.value,
                    shareableFact.url
                )
            )
            .startChooser()
    }

    private companion object {
        const val SHARE_TYPE = "text/plain"
    }
}
