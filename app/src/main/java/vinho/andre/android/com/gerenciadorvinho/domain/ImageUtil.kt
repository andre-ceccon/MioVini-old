package vinho.andre.android.com.gerenciadorvinho.domain

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageUtil(
    var currentPathImage: String,
    var nameImage: String
) : Parcelable {

    @IgnoredOnParcel
    lateinit var nameOldImage: String
}