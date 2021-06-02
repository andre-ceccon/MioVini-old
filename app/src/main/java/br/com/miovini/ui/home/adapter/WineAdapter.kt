package br.com.miovini.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.miovini.R
import br.com.miovini.databinding.RowHomeCardWineBinding
import br.com.miovini.models.Wine

class WineAdapter : ListAdapter<Wine, WineAdapter.WineViewHolder>(diffCalback) {

    var wineItemClickListener: (() -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): WineViewHolder = WineViewHolder.create(
        parent = parent, wineItemClickListener = wineItemClickListener
    )

    override fun onBindViewHolder(
        holder: WineViewHolder, position: Int
    ) = holder.bind(wine = getItem(position))

    companion object {
        private val diffCalback = object : DiffUtil.ItemCallback<Wine>() {
            override fun areItemsTheSame(
                oldItem: Wine, newItem: Wine
            ): Boolean = oldItem.name == newItem.name

            override fun areContentsTheSame(
                oldItem: Wine, newItem: Wine
            ): Boolean = oldItem == newItem
        }
    }

    class WineViewHolder(
        private val itemBinding: RowHomeCardWineBinding,
        private val wineItemClickListener: (() -> Unit)? = null
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(
            wine: Wine
        ) {
            itemBinding.run {
                tvWineName.text = wine.name
                tvVintage.text = wine.vintage
                tvCountry.text = wine.country
                tvCellar.text = wine.wineHouse
                ratingBarWine.rating = wine.rating
                if (wine.bookmark) bookmark.setImageResource(R.drawable.bookmark_24)
                else bookmark.setImageResource(R.drawable.bookmark_border_24)

                root.setOnClickListener { wineItemClickListener?.invoke() }
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                wineItemClickListener: (() -> Unit)? = null
            ): WineViewHolder {
                val itemBinding = RowHomeCardWineBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )

                return WineViewHolder(
                    itemBinding = itemBinding, wineItemClickListener = wineItemClickListener
                )
            }
        }
    }
}