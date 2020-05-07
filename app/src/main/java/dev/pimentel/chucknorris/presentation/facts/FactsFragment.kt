package dev.pimentel.chucknorris.presentation.facts

import androidx.core.app.ShareCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.databinding.FactsLayoutBinding
import dev.pimentel.chucknorris.presentation.facts.mappers.ShareableFact
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
        FactsLayoutBinding.inflate(layoutInflater)
    ) {
        factsRvFacts.also {
            it.adapter = adapter.apply {
                onItemClick = viewModel::getShareableFact
            }
            it.layoutManager = LinearLayoutManager(requireContext())
        }

        factsFabGoToSearch.setOnClickListener {
            viewModel.navigateToSearch()
        }

        factsTvError.setOnClickListener {
            viewModel.getSearchTermAndFacts()
        }

        viewModel.factsState().observe { state ->
            adapter.submitList(state.facts)
            factsTvFirstAccess.isVisible = state.isFirstAccess
            factsAblSearchTerm.isVisible = state.hasFacts
            factsTvSearchTerm.text = state.searchTerm
            factsRvFacts.isVisible = state.hasFacts
            factsTvError.isVisible = state.hasError
            factsTvError.text = getString(R.string.facts_tv_error_message, state.errorMessage)
            factsRvFacts.isVisible = state.hasFacts
            factsTvListIsEmpty.isVisible = state.isEmpty
            factsLoading.root.isVisible = state.isLoading
        }

        viewModel.shareableFact().observe(::shareFact)

        viewModel.getSearchTermAndFacts()
    }

    private fun shareFact(shareableFact: ShareableFact) {
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
