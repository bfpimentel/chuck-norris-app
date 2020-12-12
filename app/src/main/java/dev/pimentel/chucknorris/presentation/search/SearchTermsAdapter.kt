package dev.pimentel.chucknorris.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.pimentel.chucknorris.databinding.SearchLastTermsItemBinding

class SearchTermsAdapter : ListAdapter<String, SearchTermsAdapter.ViewHolder>(DIFF_CALLBACK) {

    lateinit var onItemClick: (String) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            SearchLastTermsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: SearchLastTermsItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(term: String) {
            binding.root.also {
                it.text = term
                it.setOnClickListener { onItemClick(term) }
            }
        }
    }

    private companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
            override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        }
    }
}
