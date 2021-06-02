package br.com.miovini.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.miovini.databinding.RowFilterHomeWineBinding
import br.com.miovini.models.Sort

class SortAdapter : ListAdapter<Sort, SortAdapter.SortViewHolder>(diffCalback) {

    var sortItemClickListener: (() -> Unit)? = null

    companion object {
        private val diffCalback = object : DiffUtil.ItemCallback<Sort>() {
            override fun areItemsTheSame(
                oldItem: Sort, newItem: Sort
            ): Boolean = oldItem.titleSort == newItem.titleSort

            override fun areContentsTheSame(
                oldItem: Sort, newItem: Sort
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): SortViewHolder = SortViewHolder.create(
        parent = parent, sortItemClickListener = sortItemClickListener
    )

    override fun onBindViewHolder(
        holder: SortViewHolder, position: Int
    ) = holder.bind(sort = getItem(position))

    class SortViewHolder(
        private val itemBinding: RowFilterHomeWineBinding,
        private val sortItemClickListener: (() -> Unit)? = null
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(
            sort: Sort
        ) {
            itemBinding.run {
//                tvTitleSort.text = sort.titleSort
            }

            itemBinding.tvSort.setOnClickListener {
                sortItemClickListener?.invoke()
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                sortItemClickListener: (() -> Unit)? = null
            ): SortViewHolder {
                val itemBinding = RowFilterHomeWineBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )

                return SortViewHolder(
                    itemBinding = itemBinding, sortItemClickListener = sortItemClickListener
                )
            }
        }
    }
}