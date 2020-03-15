package vinho.andre.android.com.gerenciadorvinho.presenter

import android.content.Context
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.data.firebase.WineDetailsData
import vinho.andre.android.com.gerenciadorvinho.domain.Comment
import vinho.andre.android.com.gerenciadorvinho.domain.Purchase
import vinho.andre.android.com.gerenciadorvinho.domain.Wine
import vinho.andre.android.com.gerenciadorvinho.domain.WineComplement
import vinho.andre.android.com.gerenciadorvinho.interfaces.data.WineDetailsDataInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.WineDetailsPresenterInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.view.WineDetailsActivityInterface
import vinho.andre.android.com.gerenciadorvinho.util.function.checkConnection
import java.util.*

class WineDetailsPresenter(
    val view: WineDetailsActivityInterface
) : WineDetailsPresenterInterface {

    private val data: WineDetailsDataInterface =
        WineDetailsData(
            view.getContext(),
            this
        )

    override fun deleteWine(
        idWine: String,
        context: Context,
        image: Map<String, Any>
    ) {
        if (connectionValidation(context)) {
            view.showProxy(true)
            data.deleteWine(
                idWine,
                image
            )
        }
    }

    override fun saveBookmark(
        idWine: String,
        context: Context,
        bookmark: Boolean
    ) {
        if (connectionValidation(context)) {
            view.showProxy(true)
            view.blockFields(true)

            data.saveBookmark(
                idWine,
                bookmark
            )
        }
    }

    override fun deleteComment(
        idWine: String,
        context: Context,
        idComment: String
    ) {
        if (connectionValidation(context)) {
            view.showProxy(true)
            data.deleteComment(
                idWine,
                idComment
            )
        }
    }

    override fun deletePurchase(
        idWine: String,
        context: Context,
        idPurchase: String
    ) {
        if (connectionValidation(context)) {
            view.showProxy(true)
            data.deletePurchase(
                idWine,
                idPurchase
            )
        }
    }

    override fun modifyWineHouse(
        idWine: String,
        wineHouse: Int,
        context: Context,
        idWineComplement: String
    ) {
        if (connectionValidation(context)) {
            view.showProxy(true)
            view.blockFields(true)

            data.modifyWineHouse(
                idWine,
                idWineComplement,
                wineHouse
            )
        }
    }

    override fun responseDelete(
        task: Task<Void>,
        whichItem: String
    ) {
        view.showProxy(false)
        view.blockFields(false)

        if (task.isSuccessful) {
            when (whichItem) {
                Comment.ParcelableComment -> {
                    view.snackBarFeedback(
                        view.getContext().getString(R.string.successfully_deleted)
                    )
                }
                else -> {
                    view.toastFeedback(
                        view.getContext().getString(R.string.successfully_deleted)
                    )

                    if (whichItem == Purchase.ParcelablePurchase) {
                        view.openDialogPurchase(null)
                    } else if (whichItem == Wine.ParcelableWine) {
                        view.finishDetailActivity()
                    }
                }
            }
        } else {
            view.snackBarFeedback(
                view.getContext().getString(R.string.error_delete)
            )

        }
    }

    override fun responseRetrive(
        querySnapshot: QuerySnapshot
    ) {
        for (document in querySnapshot) {
            view.setTextWineComplement(
                document.toObject(WineComplement::class.java)
            )
        }
    }

    override fun responseSaveBookmark(
        task: Task<Void>
    ) {
        view.showProxy(false)
        view.blockFields(false)
        view.updateSaveBookmark(
            task.isSuccessful
        )
    }

    override fun responseModifyWineHouse(
        task: Task<Void>,
        wineHouse: Int,
        date: Date
    ) {
        view.showProxy(false)
        view.blockFields(false)
        view.updateTextWineHouse(
            date,
            wineHouse,
            task.isSuccessful
        )
    }

    override fun retriveWineComplement(
        idWine: String
    ) = data.retriveWineComplement(
        idWine
    )

    override fun getFirestoreRecyclerOptionsComments(
        idWine: String
    ): FirestoreRecyclerOptions<Comment> {
        return data.getFirestoreRecyclerOptionsComments(
            idWine
        )
    }

    override fun getFirestoreRecyclerOptionsPurchases(
        idWine: String
    ): FirestoreRecyclerOptions<Purchase> {
        return data.getFirestoreRecyclerOptionsPurchases(
            idWine
        )
    }

    private fun connectionValidation(
        context: Context
    ): Boolean {
        return if (checkConnection(context)) {
            true
        } else {
            view.snackBarFeedback(view.getContext().getString(R.string.no_internet_connection))
            false
        }
    }
}