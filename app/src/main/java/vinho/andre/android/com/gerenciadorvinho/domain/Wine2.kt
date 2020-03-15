package vinho.andre.android.com.gerenciadorvinho.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Wine2(
    var id: String = "",
    var nome: String = "",
    var origem: String = "",
    var uva: String = "",
    var harmonizacao: String = "",
    var comentario: String = "",
    var pathimgcelular: String = "",
    var produtor: String = "",
    var safra: Int = 0,
    var temp: Int = 0,
    var adega: Int = 0,
    var favorito: Boolean = false,
    var avaliacao: Float = 0.toFloat()
) : Parcelable