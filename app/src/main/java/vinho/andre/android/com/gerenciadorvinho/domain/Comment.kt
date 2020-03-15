package vinho.andre.android.com.gerenciadorvinho.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Comment(
    var id: String = "",
    var date: Date = Date(),
    var comment: String = ""
) : Parcelable{

    companion object {
        const val updateComment = "editComment"
        const val ParcelableComment = "comment-key"
    }

    fun getMap(): Map<String, Any> {
        val map = HashMap<String, Any>()
        map["id"] = this.id
        map["date"] = this.date
        map["comment"] = this.comment
        return map
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + comment.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Comment
        if (id != other.id) return false
        if (date != other.date) return false
        if (comment != other.comment) return false

        return true
    }
}