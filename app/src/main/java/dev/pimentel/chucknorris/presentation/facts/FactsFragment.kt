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
import dev.pimentel.chucknorris.presentation.facts.data.ShareableFact
import dev.pimentel.chucknorris.presentation.search.SearchFragment
import dev.pimentel.chucknorris.shared.extensions.lifecycleBinding
import dev.pimentel.chucknorris.shared.extensions.watch
import dev.pimentel.chucknorris.shared.mvi.handleEvent
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
            state.factsEvent.handleEvent { facts ->
                adapter.submitList(facts)
                binding.factsList.scrollToPosition(0)
            }

            binding.loading.isVisible = state.isLoading

            binding.apply {
                searchTermContainer.isVisible = state.hasFacts
                searchTerm.text = state.searchTerm
                factsList.isVisible = state.hasFacts
                firstAccessText.isVisible = state.isFirstAccess
                emptyListText.isVisible = state.isEmpty
                errorText.isVisible = state.hasError

                state.errorEvent.handleEvent { errorMessage ->
                    errorText.text = getString(R.string.facts_tv_error_message, errorMessage)
                }

                state.shareFactEvent.handleEvent(::shareFact)
            }
        }
    }

    private fun bindInputs() {
        bindResultListener()

        binding.apply {
            factsList.also {
                it.adapter = adapter.apply {
                    onItemClick = { id -> viewModel.publish(FactsIntention.ShareFact(id = id)) }
                }
                it.layoutManager = LinearLayoutManager(requireContext())
            }

            search.setOnClickListener {
                viewModel.publish(FactsIntention.NavigateToSearch)
            }

            errorText.setOnClickListener {
                viewModel.publish(FactsIntention.GetLastSearchAndFacts)
            }
        }

        viewModel.publish(FactsIntention.GetLastSearchAndFacts)
    }

    private fun bindResultListener() {
        parentFragmentManager.setFragmentResultListener(
            RESULT_LISTENER_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            viewModel.publish(
                FactsIntention.NewSearch(bundle.get(SearchFragment.NEW_SEARCH_KEY) as String)
            )
        }
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

    companion object {
        const val RESULT_LISTENER_KEY = "FACTS_FRAGMENT_RESULT_LISTENER"
        private const val SHARE_TYPE = "text/plain"
    }
}
