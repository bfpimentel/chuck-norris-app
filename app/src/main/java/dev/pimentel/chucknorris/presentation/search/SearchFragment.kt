package dev.pimentel.chucknorris.presentation.search

import android.app.Activity
import android.text.InputFilter
import android.text.Spanned
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

        searchEtSearchTerm.apply {
            filters = arrayOf(EmojiFilter())
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.saveSearchTerm(text.toString())
                    hideKeyboard()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
        }

        searchTvError.setOnClickListener {
            viewModel.getCategorySuggestionsAndSearchTerms()
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
            searchCgSuggestions.isVisible = true
        }

        viewModel.searchTerms().observe(adapter::submitList)

        viewModel.selectedSuggestionIndex().observe { index ->
            searchCgSuggestions[index].isSelected = true
        }

        viewModel.isLoading().observe { searchLoading.root.isVisible = true }

        viewModel.isNotLoading().observe { searchLoading.root.isVisible = false }

        viewModel.error().observe { errorMessage ->
            searchTvError.text = getString(R.string.facts_tv_error_message, errorMessage)
            searchTvError.isVisible = true
            searchCgSuggestions.isVisible = false
        }

        viewModel.getCategorySuggestionsAndSearchTerms()
    }

    private fun hideKeyboard() {
        (requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .also { inputMethodManager ->
                inputMethodManager.hideSoftInputFromWindow(view!!.rootView.windowToken, 0)
            }
    }

    private class EmojiFilter : InputFilter {

        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            for (index in start until end) {
                val type = Character.getType(source[index])
                if (type == Character.SURROGATE.toInt() || type == Character.OTHER_SYMBOL.toInt()) {
                    return ""
                }
            }
            return null
        }
    }
}
