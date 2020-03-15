package vinho.andre.android.com.gerenciadorvinho.view.adaprers.mainactivity

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.domain.Wine
import vinho.andre.android.com.gerenciadorvinho.interfaces.view.MainActivity

class SearchAdapter(
    var context: Context,
    private var listWine: List<Wine>,
    var view: MainActivity
) : RecyclerView.Adapter<WineViweHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WineViweHolder {
        return WineViweHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.adapter_wine,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(
        holder: WineViweHolder,
        position: Int
    ) {
        holder.setText(listWine[position])
        holder.itemView.setOnClickListener {
            view.callDetailsActivity(listWine[position])
        }
    }

    override fun getItemCount(): Int = listWine.size

    fun updateList(
        newListWine: List<Wine>
    ) {
        this.listWine = newListWine
        this.notifyDataSetChanged()
    }
}