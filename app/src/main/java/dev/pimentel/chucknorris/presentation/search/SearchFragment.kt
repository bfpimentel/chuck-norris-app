package dev.pimentel.chucknorris.presentation.search

import android.app.Activity
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import dev.pimentel.chucknorris.R
import dev.pimentel.chucknorris.databinding.SearchCategoriesItemLayoutBinding
import dev.pimentel.chucknorris.databinding.SearchLayoutBinding
import dev.pimentel.chucknorris.shared.abstractions.BaseFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.module.Module

class SearchFragment : BaseFragment<SearchContract.ViewModel, SearchLayoutBinding>(
    R.layout.search_layout
) {

    override val module: Module = searchModule
    override val viewModel: SearchContract.ViewModel by viewModel<SearchViewModel>()
    private val adapter: SearchTermsAdapter by inject()

    override fun bindView() = initBinding(
        SearchLayoutBinding.inflate(layoutInflater)
    ) {
        searchRvLastSearchTerms.also {
            it.adapter = adapter.apply {
                onItemClick = viewModel::saveSearchTerm
            }
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(requireContext())
        }

        searchIlSearchTerm.setEndIconOnClickListener {
            viewModel.saveSearchTerm(searchEtSearchTerm.text.toString())
            hideKeyboard()
        }

        searchEtSearchTerm.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.saveSearchTerm(searchEtSearchTerm.text.toString())
                hideKeyboard()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        viewModel.categorySuggestions().observe { categorySuggestions ->
            categorySuggestions.forEach { suggestion ->
                val chipBinding = SearchCategoriesItemLayoutBinding.inflate(layoutInflater)
                chipBinding.searchCategoriesItemChip.apply {
                    text = suggestion
                    setOnClickListener { viewModel.saveSearchTerm(suggestion) }
                }
                searchCgSuggestions.addView(chipBinding.root)
            }
        }

        viewModel.searchTerms().observe(adapter::submitList)

        viewModel.selectedSuggestionIndex().observe { index ->
            searchCgSuggestions[index].isSelected = true
        }

        viewModel.isLoading().observe { searchLoading.root.isVisible = true }

        viewModel.isNotLoading().observe { searchLoading.root.isVisible = false }

        viewModel.setupSearch()
    }

    private fun hideKeyboard() {
        (requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .also { inputMethodManager ->
                inputMethodManager.hideSoftInputFromWindow(view!!.rootView.windowToken, 0)
            }
    }
}
