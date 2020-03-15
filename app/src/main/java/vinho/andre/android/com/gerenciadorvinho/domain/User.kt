package vinho.andre.android.com.gerenciadorvinho.domain

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(
    var id: String,
    var name: String?,
    var email: String?,
    var image: Uri?,
    var provaider: String
) : Parcelable {

    companion object {
        const val KEY = "user-key"
    }
}