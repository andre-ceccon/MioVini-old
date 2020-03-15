package vinho.andre.android.com.gerenciadorvinho.view.adaprers.winedetailsactivity

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.domain.Comment
import vinho.andre.android.com.gerenciadorvinho.interfaces.view.WineDetailsActivityInterface
import vinho.andre.android.com.gerenciadorvinho.util.DataUtil

class CommentViewHolder(
    itemView: View,
    var view: WineDetailsActivityInterface
) : RecyclerView.ViewHolder(itemView) {

    private val dataUtil = DataUtil()
    private val date: TextView =
        itemView.findViewById(R.id.tt_data)
    private val comment: TextView =
        itemView.findViewById(R.id.tt_comentario)
    private val buttonUpdate: ImageButton =
        itemView.findViewById(R.id.ibt_update_comment)
    private val buttonDelete: ImageButton =
        itemView.findViewById(R.id.ibt_delete_comment)

    fun setText(
        comment: Comment
    ) {
        date.text = dataUtil.dateToString(comment.date)
        this.comment.text = comment.comment

        buttonUpdate.setOnClickListener {
            view.callUpdateComment(
                comment
            )
        }

        buttonDelete.setOnClickListener {
            view.deleteComment(
                comment
            )
        }

        this.comment.setOnClickListener {
            if (this.comment.maxLines == 2) {
                this.comment.maxLines = 20
            } else {
                this.comment.maxLines = 2
            }
        }
    }
}