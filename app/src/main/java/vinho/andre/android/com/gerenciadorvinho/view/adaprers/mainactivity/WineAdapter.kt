package vinho.andre.android.com.gerenciadorvinho.view.adaprers.mainactivity

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestoreException
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.data.sqlite.DBHelper
import vinho.andre.android.com.gerenciadorvinho.domain.Wine
import vinho.andre.android.com.gerenciadorvinho.interfaces.view.MainActivity

class WineAdapter(
    options: FirestoreRecyclerOptions<Wine>,
    var view: MainActivity
) : FirestoreRecyclerAdapter<Wine, WineViweHolder>(options) {

    override fun onBindViewHolder(
        holder: WineViweHolder,
        position: Int,
        model: Wine
    ) {
        holder.setText(model)
        holder.itemView.setOnClickListener {
            view.callDetailsActivity(
                model
            )
        }
    }

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

    override fun onDataChanged() {
        super.onDataChanged()

        view.showProxy(false)
        view.updateToolbarSubTitle()

        if (itemCount == 0) {
            val context: Context = view.getContext()

            if (DBHelper(view.getContext()).getSumWine() == 0) {
                view.createDialogOfNoWineRegistration(
                    context.getString(R.string.title_firt_login),
                    context.getString(R.string.alert_menssage_firt_login),
                    context.getString(R.string.cancel_buttom)
                )
            } else {
                view.createDialogOfNoWineRegistration(
                    context.getString(R.string.title_alertDialog_without_wine),
                    context.getString(R.string.alert_message_dialogue_without_wine),
                    context.getString(R.string.change_filter)
                )
            }
        }
    }

    override fun onError(
        e: FirebaseFirestoreException
    ) {
        Log.e("error", e.message.toString())
    }
}