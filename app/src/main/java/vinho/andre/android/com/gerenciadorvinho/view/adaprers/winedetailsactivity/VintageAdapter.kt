package vinho.andre.android.com.gerenciadorvinho.view.adaprers.winedetailsactivity

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vinho.andre.android.com.gerenciadorvinho.R

class VintageAdapter(
    var context: Context,
    var listWine: List<String>
) : RecyclerView.Adapter<VintageViweHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VintageViweHolder {
        return VintageViweHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.adapter_vintage,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(
        holder: VintageViweHolder,
        position: Int
    ) {
        holder.setText(listWine[position])
    }

    override fun getItemCount(): Int = listWine.size
}