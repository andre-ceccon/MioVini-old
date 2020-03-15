package vinho.andre.android.com.gerenciadorvinho.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Wine(
    var id: String = "",
    var name: String = "",
    var country: String = "",
    var image: Map<String, String> = emptyMap(),
    var vintage: Int = 0,
    var wineHouse: Int = 0,
    var bookmark: Boolean = false,
    var rating: Float = 0.toFloat()
) : Parcelable {

    companion object {
        const val UrlImage = "url"
        const val NameImage = "name"
        const val FieldImage = "image"
        const val UpdateWine = "wineBase"
        const val ParcelableWine = "wine-key"
    }

    override fun equals(
        other: Any?
    ): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Wine

        if (id != other.id) return false
        if (name != other.name) return false
        if (country != other.country) return false
        if (image != other.image) return false
        if (vintage != other.vintage) return false
        if (wineHouse != other.wineHouse) return false
        if (bookmark != other.bookmark) return false
        if (rating != other.rating) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + country.hashCode()
        result = 31 * result + image.hashCode()
        result = 31 * result + vintage
        result = 31 * result + wineHouse
        result = 31 * result + bookmark.hashCode()
        result = 31 * result + rating.hashCode()
        return result
    }

    fun getMap(): Map<String, Any> {
        val map = HashMap<String, Any>()
        map["id"] = this.id
        map["name"] = this.name
        map["country"] = this.country
        map["image"] = this.image
        map["vintage"] = this.vintage
        map["wineHouse"] = this.wineHouse
        map["bookmark"] = this.bookmark
        map["rating"] = this.rating
        return map
    }
}