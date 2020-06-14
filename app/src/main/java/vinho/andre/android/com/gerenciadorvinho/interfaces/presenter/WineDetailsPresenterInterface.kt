package vinho.andre.android.com.gerenciadorvinho.interfaces.presenter

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import vinho.andre.android.com.gerenciadorvinho.domain.Comment
import vinho.andre.android.com.gerenciadorvinho.domain.Purchase
import java.util.*

interface WineDetailsPresenterInterface {
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
        idPurchase: String
    )

    fun retriveWineComplement(
        idWine: String
    )

    fun modifyWineHouse(
        idWine: String,
        wineHouse: Int,
        idWineComplement: String
    )

    fun saveBookmark(
        idWine: String,
        bookmark: Boolean
    )

    fun responseModifyWineHouse(
        task: Task<Void>,
        wineHouse: Int,
        date: Date
    )

    fun responseSaveBookmark(
        task: Task<Void>
    )

    fun responseDelete(
        task: Task<Void>?,
        whichItem: String
    )

    fun responseRetrive(
        querySnapshot: QuerySnapshot
    )

    fun getFirestoreRecyclerOptionsComments(
        idWine: String
    ): FirestoreRecyclerOptions<Comment>

    fun getFirestoreRecyclerOptionsPurchases(
        idWine: String
    ): FirestoreRecyclerOptions<Purchase>
}