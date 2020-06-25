package vinho.andre.android.com.gerenciadorvinho.view.adaprers.winedetailsactivity

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestoreException
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.domain.Comment
import vinho.andre.android.com.gerenciadorvinho.interfaces.view.WineDetailsActivityInterface

class CommentAdapter(
    options: FirestoreRecyclerOptions<Comment>,
    var view: WineDetailsActivityInterface
) : FirestoreRecyclerAdapter<
        Comment,
        CommentViewHolder>(options) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentViewHolder {
        return CommentViewHolder(
            LayoutInflater
                .from(
                    parent.context
                ).inflate(
                    R.layout.adapter_comment,
                    parent,
                    false
                ),
            view
        )
    }

    override fun onBindViewHolder(
        holder: CommentViewHolder,
        position: Int,
        model: Comment
    ) {
        holder.setText(model)
    }

    override fun onDataChanged() {
        super.onDataChanged()
        view.showProxy(false)
        if (itemCount > 0) {
            view.showRecyclerViewComments(true)
        }
    }

    override fun onError(
        e: FirebaseFirestoreException
    ) {
        Log.e("error", e.message.toString())
    }
}