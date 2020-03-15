package vinho.andre.android.com.gerenciadorvinho.view.adaprers.mainactivity

import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.domain.Wine

class WineViweHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private var name: TextView = itemView.findViewById(R.id.tv_name)
    private var vintage: TextView = itemView.findViewById(R.id.tv_vintage)
    private var origin: TextView = itemView.findViewById(R.id.tv_country)
    private var wineHouse: TextView = itemView.findViewById(R.id.tv_wineHouse)
    private var ratingWine: RatingBar = itemView.findViewById(R.id.ratingWine)
    private var bookmark: RatingBar = itemView.findViewById(R.id.bookmark)
    private var image: RoundedImageView = itemView.findViewById(R.id.iv_wine)

    fun setText(wine: Wine) {
        name.text = wine.name
        vintage.text = if (wine.vintage == 0) "" else wine.vintage.toString()
        origin.text = wine.country
        wineHouse.text = wine.wineHouse.toString()
        ratingWine.rating = wine.rating
        bookmark.visibility = if (wine.bookmark) View.VISIBLE else View.GONE

        if (wine.image.isNotEmpty() &&
            wine.image[Wine.UrlImage]?.isNotBlank()!!
        ) {
            Picasso
                .get()
                .load(
                    wine.image[Wine.UrlImage]
                )
                .placeholder(
                    R.drawable.logo
                )
                .fit()
                .into(image)
        } else {
            Picasso
                .get()
                .load(
                    R.drawable.logo
                )
                .fit()
                .into(image)
        }
    }
}