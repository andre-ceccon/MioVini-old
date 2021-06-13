package br.com.miovini.ui.home.adapter

import android.graphics.drawable.GradientDrawable
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.util.isEmpty
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.miovini.R
import br.com.miovini.databinding.RowHomeCardWineBinding
import br.com.miovini.models.Wine
import br.com.miovini.ui.home.isDarkTheme

class WineAdapter : ListAdapter<Wine, WineAdapter.WineViewHolder>(diffCalback) {

    private var currentSelectPos: Int = -1
    val selectedItems = SparseBooleanArray()

    var wineItemClickListener: ((Wine) -> Unit)? = null
    var wineItemLongClickListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): WineViewHolder = WineViewHolder.create(parent = parent)

    fun toggleSelection(
        position: Int
    ) {
        currentSelectPos = position
        if (selectedItems[position, false]) {
            selectedItems.delete(position)
            getItem(position).selectedItem = false
        } else {
            selectedItems.put(position, true)
            getItem(position).selectedItem = true
        }

        notifyItemChanged(position)
    }

    override fun onBindViewHolder(
        holder: WineViewHolder, position: Int
    ) {
        holder.bind(wine = getItem(position))

        holder.itemView.setOnClickListener {
            if (selectedItems.isEmpty()) wineItemClickListener?.invoke(getItem(position))
            else wineItemLongClickListener?.invoke(position)
        }

        holder.itemView.setOnLongClickListener {
            wineItemLongClickListener?.invoke(position); true
        }
    }

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
        private val itemBinding: RowHomeCardWineBinding
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
                bookmark.setImageResource(
                    if (wine.bookmark) R.drawable.bookmark_24 else R.drawable.bookmark_border_24
                )

                changeColorDrawable()
                changeColorSelected(wine.selectedItem)
            }
        }

        private fun changeColorSelected(
            isSelected: Boolean
        ) {
            itemBinding.clWineAdapter.background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(
                    ContextCompat.getColor(
                        itemView.context,
                        if (isSelected)
                            R.color.selecao
                        else {
                            if (itemView.context.isDarkTheme()) R.color.backgroundCardNight
                            else R.color.backgroundCardDay
                        }
                    )
                )
            }
        }

        private fun changeColorDrawable() {
            DrawableCompat.setTint(
                DrawableCompat.wrap(itemBinding.bookmark.drawable),
                setColor(isNight = itemView.context.isDarkTheme())
            )
        }

        private fun setColor(
            isNight: Boolean
        ): Int {
            return ContextCompat.getColor(
                itemView.context, if (isNight)
                    R.color.colorOnPrimaryNight
                else R.color.colorPrimaryDay
            )
        }

        companion object {
            fun create(
                parent: ViewGroup
            ): WineViewHolder {
                val itemBinding = RowHomeCardWineBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )

                return WineViewHolder(itemBinding = itemBinding)
            }
        }
    }
}