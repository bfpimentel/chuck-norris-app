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
import dev.pimentel.chucknorris.shared.mvi.handleEvent
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
        bindRecyclerView()
        bindOutputs()
        bindInputs()
    }

    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(searchModule)
    }

    private fun bindRecyclerView() {
        binding.lastSearches.also {
            it.adapter = this@SearchFragment.adapter
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun bindOutputs() {
        watch(viewModel.state()) { state ->
            fillCategorySuggestions(state.categorySuggestions)

            state.searchTermsEvent.handleEvent(adapter::submitList)

            binding.loading.isVisible = state.isLoading

            binding.apply {
                suggestions.isVisible = state.hasSuggestions
                errorText.isVisible = state.hasError

                state.newSearch.handleEvent(::setNewSearchResult)

                state.selectSuggestionEvent.handleEvent { index ->
                    suggestions[index].isSelected = true
                }

                state.errorEvent.handleEvent { errorMessage ->
                    errorText.text = getString(R.string.facts_tv_error_message, errorMessage)
                }
            }
        }
    }

    private fun bindInputs() {
        prepareSearchInput()

        adapter.onItemClick = { term ->
            viewModel.publish(SearchIntention.SaveSearchTerm(term = term))
        }

        binding.apply {
            searchTermInputLayout.setEndIconOnClickListener {
                viewModel.publish(
                    SearchIntention.SaveSearchTerm(term = searchTermInput.text.toString())
                )
                hideKeyboard()
            }

            errorText.setOnClickListener {
                viewModel.publish(SearchIntention.GetCategorySuggestionsAndSearchTerms)
            }
        }

        viewModel.publish(SearchIntention.GetCategorySuggestionsAndSearchTerms)
    }

    private fun fillCategorySuggestions(categorySuggestions: List<String>) {
        categorySuggestions.forEach { suggestion ->
            val chipBinding = SearchCategoriesItemBinding.inflate(layoutInflater)
            chipBinding.root.apply {
                text = suggestion
                setOnClickListener {
                    viewModel.publish(SearchIntention.SaveSearchTerm(term = suggestion))
                }
            }
            binding.suggestions.addView(chipBinding.root)
        }
    }

    private fun setNewSearchResult(term: String) {
        parentFragmentManager.setFragmentResult(
            FactsFragment.RESULT_LISTENER_KEY,
            bundleOf(NEW_SEARCH_KEY to term)
        )
    }

    private fun prepareSearchInput() {
        binding.searchTermInput.apply {
            filters = arrayOf(EmojiFilter)
            setOnEditorActionListener { view, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.publish(SearchIntention.SaveSearchTerm(term = view.text.toString()))
                    hideKeyboard()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
        }
    }

    private fun hideKeyboard() {
        (requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .also { inputMethodManager ->
                inputMethodManager.hideSoftInputFromWindow(requireView().rootView.windowToken, 0)
            }
    }

    companion object {
        const val NEW_SEARCH_KEY = "SEARCH_NEW_SEARCH"
    }
}
