package vinho.andre.android.com.gerenciadorvinho.interfaces.data

import vinho.andre.android.com.gerenciadorvinho.domain.Comment
import vinho.andre.android.com.gerenciadorvinho.domain.Purchase
import vinho.andre.android.com.gerenciadorvinho.domain.Wine
import vinho.andre.android.com.gerenciadorvinho.domain.WineComplement

interface WineRegisterDataInterface {
    fun onSaveWine(
        wine: Wine,
        comment: Comment,
        purchase: Purchase,
        wineComplement: WineComplement
    )

    fun onSaveWine(
        wine: Wine,
        purchase: Purchase,
        wineComplement: WineComplement
    )

    fun onSaveWine(
        wine: Wine,
        comment: Comment,
        wineComplement: WineComplement
    )

    fun onSaveWine(
        wine: Wine,
        wineComplement: WineComplement
    )

    fun onUploadImage()
}