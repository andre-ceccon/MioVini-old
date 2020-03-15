package vinho.andre.android.com.gerenciadorvinho.interfaces.data

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import vinho.andre.android.com.gerenciadorvinho.domain.Comment
import vinho.andre.android.com.gerenciadorvinho.domain.Purchase

interface WineDetailsDataInterface {
    fun retriveWineComplement(
        idWine: String
    )

    fun modifyWineHouse(
        idWine: String,
        idWineComplement: String,
        wineHouse: Int
    )

    fun saveBookmark(
        idWine: String,
        bookmark: Boolean
    )

    fun getFirestoreRecyclerOptionsComments(
        idWine: String
    ): FirestoreRecyclerOptions<Comment>

    fun getFirestoreRecyclerOptionsPurchases(
        idWine: String
    ): FirestoreRecyclerOptions<Purchase>

    fun deleteWine(
        idWine: String,
        image: Map<String, Any>
    )

    fun deleteComment(
        idWine: String,
        idComment: String
    )

    fun deletePurchase(
        idWine: String,
        idPurchse: String
    )
}