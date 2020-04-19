package dev.pimentel.chucknorris.presentation.facts

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.pimentel.chucknorris.databinding.FactsItemLayoutBinding

class FactsAdapter : ListAdapter<FactsViewModel.FactDisplay, FactsAdapter.ViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            FactsItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class ViewHolder(
        private val binding: FactsItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(factDisplay: FactsViewModel.FactDisplay) {
            binding.factsItemTvValue.let {
                it.text = factDisplay.value
                it.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    itemView.resources.getDimension(factDisplay.fontSize)
                )
                it.setOnClickListener {
                }
            }
        }
    }

    private companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FactsViewModel.FactDisplay>() {
            override fun areItemsTheSame(
                oldItem: FactsViewModel.FactDisplay,
                newItem: FactsViewModel.FactDisplay
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: FactsViewModel.FactDisplay,
                newItem: FactsViewModel.FactDisplay
            ) = oldItem == newItem
        }
    }
}
