package br.com.miovini.ui.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.miovini.databinding.RowPurchasedDetailsWineBinding
import br.com.miovini.models.Purhased

class PurchasedAdapter : ListAdapter<Purhased, PurchasedAdapter.PurchasedViewHolder>(diffCalback) {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): PurchasedViewHolder = PurchasedViewHolder.create(parent = parent)

    override fun onBindViewHolder(
        holder: PurchasedViewHolder, position: Int
    ) = holder.bind(purhased = getItem(position))

    companion object {
        private val diffCalback = object : DiffUtil.ItemCallback<Purhased>() {
            override fun areItemsTheSame(
                oldItem: Purhased, newItem: Purhased
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Purhased, newItem: Purhased
            ): Boolean = oldItem == newItem
        }
    }

    class PurchasedViewHolder(
        private val itemBinding: RowPurchasedDetailsWineBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(
            purhased: Purhased
        ) {
            with(itemBinding) {
                tvNote.text = purhased.note
                tvName.text = purhased.storeName
                tvVintage.text = purhased.vintage
                tvPrice.text = purhased.price.toString()
                tvAmount.text = purhased.amount.toString()
                tvPurchasedDate.text = purhased.purchasedDate.toString()
            }
        }

        companion object {
            fun create(
                parent: ViewGroup
            ): PurchasedViewHolder {
                val itemBinding = RowPurchasedDetailsWineBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return PurchasedViewHolder(itemBinding = itemBinding)
            }
        }
    }
}