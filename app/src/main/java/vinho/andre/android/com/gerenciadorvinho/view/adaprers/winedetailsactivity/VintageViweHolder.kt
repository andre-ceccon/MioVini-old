package vinho.andre.android.com.gerenciadorvinho.view.adaprers.winedetailsactivity

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vinho.andre.android.com.gerenciadorvinho.R

class VintageViweHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val vintage: TextView =
        itemView.findViewById(R.id.tv_vintage_list)

    fun setText(
        vintage: String
    ) {
        this.vintage.text = vintage
    }
}