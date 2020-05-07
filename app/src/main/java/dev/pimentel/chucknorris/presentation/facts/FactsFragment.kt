package dev.pimentel.chucknorris.presentation.facts

import android.os.Bundle
import android.view.View
import androidx.core.app.ShareCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.databinding.FactsLayoutBinding
import dev.pimentel.chucknorris.presentation.facts.mappers.ShareableFact
import dev.pimentel.chucknorris.shared.abstractions.lifecycleBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class FactsFragment : Fragment(R.layout.facts_layout) {

    private val binding by lifecycleBinding(FactsLayoutBinding::bind)
    private val viewModel: FactsContract.ViewModel by viewModel<FactsViewModel>()
    private val adapter: FactsAdapter by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadKoinModules(factsModule)

        binding.apply {
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

            viewModel.factsState().observe(viewLifecycleOwner, Observer { state ->
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
            })

            viewModel.shareableFact().observe(viewLifecycleOwner, Observer(::shareFact))

            viewModel.getSearchTermAndFacts()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(factsModule)
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
