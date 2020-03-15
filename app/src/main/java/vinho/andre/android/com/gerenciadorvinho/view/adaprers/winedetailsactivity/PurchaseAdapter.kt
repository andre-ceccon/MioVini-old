package vinho.andre.android.com.gerenciadorvinho.view.adaprers.winedetailsactivity

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestoreException
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.domain.Purchase
import vinho.andre.android.com.gerenciadorvinho.interfaces.view.WineDetailsActivityInterface

class PurchaseAdapter(
    options: FirestoreRecyclerOptions<Purchase>,
    var view: WineDetailsActivityInterface
) : FirestoreRecyclerAdapter<
        Purchase,
        PurchaseViewHolder>(options) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PurchaseViewHolder {
        return PurchaseViewHolder(
            LayoutInflater
                .from(
                    parent.context
                ).inflate(
                    R.layout.dialog_list_preco_adapter,
                    parent,
                    false
                ),
            view
        )
    }

    override fun onBindViewHolder(
        holder: PurchaseViewHolder,
        position: Int,
        model: Purchase
    ) {
        holder.setText(model)
    }

    override fun onDataChanged() {
        super.onDataChanged()

        view.showProxy(false)

        view.modifyTitleDialogPurchase(
            itemCount
        )
    }

    override fun onError(
        e: FirebaseFirestoreException
    ) {
        Log.e("error", e.message.toString())
    }
}