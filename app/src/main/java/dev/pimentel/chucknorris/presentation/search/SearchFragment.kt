package dev.pimentel.chucknorris.presentation.search

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
        SearchLayoutBinding.inflate(layoutInflater),
        this
    ) {
        searchRvLastSearchTerms.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.categorySuggestions().observe { categorySuggestions ->
            categorySuggestions.forEach { suggestion ->
                val chipBinding = SearchCategoriesItemLayoutBinding.inflate(layoutInflater)
                chipBinding.searchCategoriesItemChip.text = suggestion
                searchCgSuggestions.addView(chipBinding.root)
            }
        }

        viewModel.searchTerms().observe { searchTerms ->
            adapter.submitList(searchTerms)
        }

        searchBtSend.setOnClickListener {
            viewModel.saveSearchTerm(searchEtSearchTerm.text.toString())
        }

        viewModel.initialize()
    }
}
