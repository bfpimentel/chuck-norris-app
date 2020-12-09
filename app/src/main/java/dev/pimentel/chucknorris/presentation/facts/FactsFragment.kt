package dev.pimentel.chucknorris.presentation.facts

import android.os.Bundle
import android.view.View
import androidx.core.app.ShareCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.databinding.FactsFragmentBinding
import dev.pimentel.chucknorris.presentation.facts.data.FactsIntention
import dev.pimentel.chucknorris.presentation.facts.mappers.ShareableFact
import dev.pimentel.chucknorris.shared.extensions.lifecycleBinding
import dev.pimentel.chucknorris.shared.extensions.watch
import dev.pimentel.chucknorris.shared.mvi.handle
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class FactsFragment : Fragment(R.layout.facts_fragment) {

    private val binding by lifecycleBinding(FactsFragmentBinding::bind)
    private val viewModel: FactsContract.ViewModel by viewModel<FactsViewModel>()
    private val adapter: FactsAdapter by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadKoinModules(factsModule)
        bindOutputs()
        bindInputs()
    }

    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(factsModule)
    }

    private fun bindOutputs() {
        watch(viewModel.state()) { state ->
            adapter.submitList(state.facts)

            binding.apply {
                factsTvFirstAccess.isVisible = state.isFirstAccess
                factsTvSearchTerm.text = state.searchTerm
                factsLoading.root.isVisible = state.isLoading

                state.emptyListEvent?.handle {
                    factsAblSearchTerm.isVisible = false
                    factsRvFacts.isVisible = false
                    factsTvListIsEmpty.isVisible = true
                } ?: run {
                    factsAblSearchTerm.isVisible = true
                    factsRvFacts.isVisible = true
                    factsTvListIsEmpty.isVisible = false
                }

                state.errorEvent?.handle { errorMessage ->
                    factsTvError.isVisible = true
                    factsTvError.text = getString(R.string.facts_tv_error_message, errorMessage)
                } ?: run {
                    factsTvError.isVisible = false
                }

                state.shareFactEvent.handle(::shareFact)
            }
        }
    }

    private fun bindInputs() {
        binding.apply {
            factsRvFacts.also {
                it.adapter = adapter.apply {
                    onItemClick = { id -> viewModel.publish(FactsIntention.ShareFact(id = id)) }
                }
                it.layoutManager = LinearLayoutManager(requireContext())
            }

            factsFabGoToSearch.setOnClickListener {
                viewModel.publish(FactsIntention.NavigateToSearch)
            }

            factsTvError.setOnClickListener {
                viewModel.publish(FactsIntention.GetSearchTermsAndFacts)
            }
        }

        viewModel.publish(FactsIntention.GetSearchTermsAndFacts)
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
