package br.com.miovini.ui.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.miovini.databinding.RowCommentsDetailsWineBinding
import br.com.miovini.models.Comment

class CommentAdapter : ListAdapter<Comment, CommentAdapter.CommentViewHolder>(diffCalback) {

    var commentItemClickListener: ((Comment) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): CommentViewHolder = CommentViewHolder.create(
        parent = parent, commentItemClickListener = commentItemClickListener
    )

    override fun onBindViewHolder(
        holder: CommentViewHolder, position: Int
    ) = holder.bind(comment = getItem(position))

    companion object {
        private val diffCalback = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(
                oldItem: Comment, newItem: Comment
            ): Boolean = oldItem.comment == newItem.comment

            override fun areContentsTheSame(
                oldItem: Comment, newItem: Comment
            ): Boolean = oldItem == newItem
        }
    }

    class CommentViewHolder(
        private val itemBinding: RowCommentsDetailsWineBinding,
        private val commentItemClickListener: ((Comment) -> Unit)? = null
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(
            comment: Comment
        ) {
            with(itemBinding) {
                tvComment.text = comment.comment
                tvDataComment.text = comment.data
                root.setOnClickListener { commentItemClickListener?.invoke(comment) }
            }
        }

        companion object {
            fun create(
                parent: ViewGroup, commentItemClickListener: ((Comment) -> Unit)? = null
            ): CommentViewHolder {
                val itemBinding = RowCommentsDetailsWineBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )

                return CommentViewHolder(
                    itemBinding = itemBinding, commentItemClickListener = commentItemClickListener
                )
            }
        }
    }
}