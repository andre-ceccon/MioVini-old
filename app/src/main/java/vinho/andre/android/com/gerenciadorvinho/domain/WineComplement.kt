package vinho.andre.android.com.gerenciadorvinho.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
open class WineComplement(
    var wineComplementId: String = "",
    var grape: String = "",
    var harmonization: String = "",
    var temperature: Int = 0,
    var producer: String = "",
    var dateWineHouse: Date = Date()
) : Parcelable {

    companion object {
        const val ParcelableWineComplement = "wineComplement-key"
    }

    fun getMap(): Map<String, Any> {
        val map = HashMap<String, Any>()
        map["wineComplementId"] = this.wineComplementId
        map["grape"] = this.grape
        map["harmonization"] = this.harmonization
        map["temperature"] = this.temperature
        map["producer"] = this.producer
        map["dateWineHouse"] = this.dateWineHouse
        return map
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WineComplement

        if (wineComplementId != other.wineComplementId) return false
        if (grape != other.grape) return false
        if (harmonization != other.harmonization) return false
        if (temperature != other.temperature) return false
        if (producer != other.producer) return false
        if (dateWineHouse != other.dateWineHouse) return false

        return true
    }

    override fun hashCode(): Int {
        var result = wineComplementId.hashCode()
        result = 31 * result + grape.hashCode()
        result = 31 * result + harmonization.hashCode()
        result = 31 * result + temperature
        result = 31 * result + producer.hashCode()
        result = 31 * result + dateWineHouse.hashCode()
        return result
    }

    override fun toString(): String {
        return "WineComplement(id='$wineComplementId', grape='$grape', harmonization='$harmonization', temperature=$temperature, producer='$producer', dateWineHouse=$dateWineHouse)"
    }
}