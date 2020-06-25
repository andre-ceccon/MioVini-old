package vinho.andre.android.com.gerenciadorvinho.interfaces.view

import android.content.Context
import android.view.View
import vinho.andre.android.com.gerenciadorvinho.domain.Comment
import vinho.andre.android.com.gerenciadorvinho.domain.Purchase
import vinho.andre.android.com.gerenciadorvinho.domain.WineComplement
import java.util.*

interface WineDetailsActivityInterface {
    fun finishDetailActivity()

    fun getContext(): Context

    fun callUpdateComment(
        comment: Comment
    )

    fun callUpdatePurchase(
        purchase: Purchase
    )

    fun deleteComment(
        comment: Comment
    )

    fun deletePurchase(
        purchase: Purchase
    )

    fun updateTextWineHouse(
        date: Date,
        wineHouse: Int,
        success: Boolean
    )

    fun updateSaveBookmark(
        success: Boolean
    )

    fun setTextWineComplement(
        wineComplement: WineComplement
    )

    fun showRecyclerViewComments(
        status: Boolean
    )

    fun showProxy(
        status: Boolean
    )

    fun snackBarFeedback(
        message: String
    )

    fun toastFeedback(
        message: String
    )

    fun blockFields(
        status: Boolean
    )

    fun getStringFormatted(
        text: String,
        field: String
    ): String

    fun modifyTitleDialogPurchase(
        itemCount: Int
    )

    fun openDialogPurchase(
        view: View?
    )
}