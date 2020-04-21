package vinho.andre.android.com.gerenciadorvinho.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Purchase(
    var vintage: Int = 0,
    var amount: Int = 0,
    var price: Double = 0.0,
    var date: Date = Date(),
    var purchaseId: String = "",
    var store: String = "",
    var comment: String = ""
) : Parcelable {

    companion object {
        const val newPurchase = "newPurchase"
        const val updatePurchase = "editPurchase"
        const val ParcelablePurchase = "purchase-key"
    }

    fun getMap(): Map<String, Any> {
        val map = HashMap<String, Any>()
        map["vintage"] = this.vintage
        map["amount"] = this.amount
        map["price"] = this.price
        map["date"] = this.date
        map["purchaseId"] = this.purchaseId
        map["store"] = this.store
        map["comment"] = this.comment
        return map
    }

    override fun hashCode(): Int {
        var result = vintage
        result = 31 * result + amount
        result = 31 * result + price.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + purchaseId.hashCode()
        result = 31 * result + store.hashCode()
        result = 31 * result + comment.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Purchase
        if (vintage != other.vintage) return false
        if (amount != other.amount) return false
        if (price != other.price) return false
        if (date != other.date) return false
        if (purchaseId != other.purchaseId) return false
        if (store != other.store) return false
        if (comment != other.comment) return false

        return true
    }
}