package dev.pimentel.chucknorris.presentation.facts

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.pimentel.chucknorris.databinding.FactsItemBinding
import dev.pimentel.chucknorris.presentation.facts.data.FactViewData

class FactsAdapter : ListAdapter<FactViewData, FactsAdapter.ViewHolder>(
    DIFF_CALLBACK
) {

    lateinit var onItemClick: (String) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            FactsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class ViewHolder(
        private val binding: FactsItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FactViewData) {
            binding.apply {
                factsItemCard.setOnClickListener {
                    onItemClick(item.id)
                }
                factsItemTvValue.let {
                    it.text = item.value
                    it.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        itemView.resources.getDimension(item.fontSize)
                    )
                }
                factsItemChipCategory.text = item.category
            }
        }
    }

    private companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FactViewData>() {
            override fun areItemsTheSame(
                oldItem: FactViewData,
                newItem: FactViewData
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: FactViewData,
                newItem: FactViewData
            ) = oldItem == newItem
        }
    }
}
