package br.com.miovini.ui.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.miovini.databinding.RowVintageDetailsWineBinding
import br.com.miovini.models.Vintage

class VintageAdapter : ListAdapter<Vintage, VintageAdapter.VintageViewHolder>(diffCalback) {

    var onItemClickListener: (() -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): VintageViewHolder = VintageViewHolder.create(
        parent = parent, onItemClickListener = onItemClickListener
    )

    override fun onBindViewHolder(
        holder: VintageViewHolder, position: Int
    ) = holder.bind(vintage = getItem(position))

    companion object {
        private val diffCalback = object : DiffUtil.ItemCallback<Vintage>() {
            override fun areItemsTheSame(
                oldItem: Vintage, newItem: Vintage
            ): Boolean = oldItem.purchaseID == newItem.purchaseID

            override fun areContentsTheSame(
                oldItem: Vintage, newItem: Vintage
            ): Boolean = oldItem == newItem
        }
    }

    class VintageViewHolder(
        private val itemBinding: RowVintageDetailsWineBinding,
        private val onItemClickListener: (() -> Unit)? = null,
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(
            vintage: Vintage
        ) {
            itemBinding.textView20.text = vintage.vintage
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onItemClickListener: (() -> Unit)? = null,
            ): VintageViewHolder {
                val itemBinding = RowVintageDetailsWineBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )

                return VintageViewHolder(
                    itemBinding = itemBinding, onItemClickListener = onItemClickListener
                )
            }
        }
    }
}