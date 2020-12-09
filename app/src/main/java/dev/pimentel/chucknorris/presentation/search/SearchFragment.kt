package dev.pimentel.chucknorris.presentation.search

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.databinding.SearchCategoriesItemBinding
import dev.pimentel.chucknorris.databinding.SearchFragmentBinding
import dev.pimentel.chucknorris.presentation.facts.FactsFragment
import dev.pimentel.chucknorris.presentation.search.data.SearchIntention
import dev.pimentel.chucknorris.shared.emoji.EmojiFilter
import dev.pimentel.chucknorris.shared.extensions.lifecycleBinding
import dev.pimentel.chucknorris.shared.extensions.watch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class SearchFragment : BottomSheetDialogFragment() {

    private val binding by lifecycleBinding(SearchFragmentBinding::bind)
    private val viewModel: SearchContract.ViewModel by viewModel<SearchViewModel>()
    private val adapter: SearchTermsAdapter by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.search_fragment, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadKoinModules(searchModule)
        bindOutputs()
        bindInputs()
    }

    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(searchModule)
    }

    private fun bindOutputs() {
        watch(viewModel.state()) { state ->
            adapter.submitList(state.searchTerms)
            fillCategorySuggestions(state.categorySuggestions)

            binding.apply {
                searchLoading.root.isVisible = state.isLoading
                searchCgSuggestions.isVisible = state.categorySuggestions.isNotEmpty()

                state.newSearch?.value?.also(::setNewSearchResult)

                state.selectSuggestionEvent?.value?.also { index ->
                    searchCgSuggestions[index].isSelected = true
                }

                state.errorEvent?.value?.also { message ->
                    searchTvError.isVisible = true
                    searchTvError.text = getString(R.string.facts_tv_error_message, message)
                } ?: run {
                    searchTvError.isVisible = false
                }
            }
        }
    }

    private fun bindInputs() {
        binding.apply {
            searchRvLastSearchTerms.also {
                it.adapter = adapter.apply {
                    onItemClick = { term ->
                        viewModel.publish(SearchIntention.SaveSearchTerm(term = term))
                    }
                }
                it.setHasFixedSize(true)
                it.layoutManager = LinearLayoutManager(requireContext())
            }

            searchIlSearchTerm.setEndIconOnClickListener {
                viewModel.publish(
                    SearchIntention.SaveSearchTerm(term = searchEtSearchTerm.text.toString())
                )
                hideKeyboard()
            }

            searchEtSearchTerm.apply {
                filters = arrayOf(EmojiFilter)
                setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        viewModel.publish(SearchIntention.SaveSearchTerm(term = text.toString()))
                        hideKeyboard()
                        return@setOnEditorActionListener true
                    }
                    return@setOnEditorActionListener false
                }
            }

            searchTvError.setOnClickListener {
                viewModel.publish(SearchIntention.GetCategorySuggestionsAndSearchTerms)
            }
        }

        viewModel.publish(SearchIntention.GetCategorySuggestionsAndSearchTerms)
    }

    private fun fillCategorySuggestions(categorySuggestions: List<String>) {
        categorySuggestions.forEach { suggestion ->
            val chipBinding = SearchCategoriesItemBinding.inflate(layoutInflater)
            chipBinding.searchCategoriesItemChip.apply {
                text = suggestion
                setOnClickListener {
                    viewModel.publish(SearchIntention.SaveSearchTerm(term = suggestion))
                }
            }
            binding.searchCgSuggestions.addView(chipBinding.root)
        }
    }

    private fun hideKeyboard() {
        (requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .also { inputMethodManager ->
                inputMethodManager.hideSoftInputFromWindow(requireView().rootView.windowToken, 0)
            }
    }

    private fun setNewSearchResult(term: String) {
        parentFragmentManager.setFragmentResult(
            FactsFragment.RESULT_LISTENER_KEY,
            bundleOf(NEW_SEARCH_KEY to term)
        )
    }

    companion object {
        const val NEW_SEARCH_KEY = "SEARCH_NEW_SEARCH"
    }
}
