package vinho.andre.android.com.gerenciadorvinho.view.adaprers.winedetailsactivity

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.domain.Purchase
import vinho.andre.android.com.gerenciadorvinho.interfaces.view.WineDetailsActivityInterface
import vinho.andre.android.com.gerenciadorvinho.util.DataUtil

class PurchaseViewHolder(
    itemView: View,
    var view: WineDetailsActivityInterface
) : RecyclerView.ViewHolder(itemView) {

    private val dataUtil = DataUtil()
    private val date: TextView =
        itemView.findViewById(R.id.tv_date_purchase)
    private val store: TextView =
        itemView.findViewById(R.id.tv_store_purchase)
    private val price: TextView =
        itemView.findViewById(R.id.tv_price_purchase)
    private val amount: TextView =
        itemView.findViewById(R.id.tv_amount_purchase)
    private val vintage: TextView =
        itemView.findViewById(R.id.tv_vintage_purchase)
    private val comment: TextView =
        itemView.findViewById(R.id.tv_comment_purchase)
    private val buttonUpdate: ImageButton =
        itemView.findViewById(R.id.ibt_update_comment)
    private val buttonDelete: ImageButton =
        itemView.findViewById(R.id.ibt_delete_comment)

    fun setText(
        purchase: Purchase
    ) {
        comment.text = purchase.comment
        date.text = dataUtil.dateToString(purchase.date)
        store.text = view.getStringFormatted(purchase.store, "store")
        price.text = view.getStringFormatted(purchase.price.toString(), "price")
        amount.text = view.getStringFormatted(purchase.amount.toString(), "amount")
        vintage.text = view.getStringFormatted(purchase.vintage.toString(), "vintage")

        buttonUpdate.setOnClickListener {
            view.callUpdatePurchase(
                purchase
            )
        }

        buttonDelete.setOnClickListener {
            view.deletePurchase(
               purchase
            )
        }
    }
}