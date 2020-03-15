package vinho.andre.android.com.gerenciadorvinho.interfaces.view

import android.content.Context
import vinho.andre.android.com.gerenciadorvinho.domain.Comment
import vinho.andre.android.com.gerenciadorvinho.domain.ImageUtil
import vinho.andre.android.com.gerenciadorvinho.domain.Purchase
import vinho.andre.android.com.gerenciadorvinho.domain.Wine
import vinho.andre.android.com.gerenciadorvinho.domain.WineComplement

interface WineRegisterViewInterface {
    fun callWineDetailsActivity(
        wine: Wine
    )

    fun getWine(): Wine

    fun getContext(): Context

    fun getComment(): Comment

    fun getImage(): ImageUtil?

    fun getPurchase(): Purchase

    fun getWineComplement(): WineComplement
}