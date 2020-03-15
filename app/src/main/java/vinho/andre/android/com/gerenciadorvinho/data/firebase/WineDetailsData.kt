package vinho.andre.android.com.gerenciadorvinho.data.firebase

import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.WriteBatch
import vinho.andre.android.com.gerenciadorvinho.data.sqlite.DBHelper
import vinho.andre.android.com.gerenciadorvinho.domain.Comment
import vinho.andre.android.com.gerenciadorvinho.domain.Purchase
import vinho.andre.android.com.gerenciadorvinho.domain.Wine
import vinho.andre.android.com.gerenciadorvinho.interfaces.data.WineDetailsDataInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.WineDetailsPresenterInterface
import vinho.andre.android.com.gerenciadorvinho.util.DataUtil
import java.util.concurrent.Callable

class WineDetailsData(
    context: Context,
    private val presenter: WineDetailsPresenterInterface
) : FirebaseBase(),
    WineDetailsDataInterface {

    private val sqlite = DBHelper(context)

    override fun retriveWineComplement(
        idWine: String
    ) {
        getWinesComplementsCollection(
            idWine
        ).get().addOnSuccessListener(
            executor,
            OnSuccessListener {
                presenter.responseRetrive(
                    it
                )
            }
        )
    }

    override fun modifyWineHouse(
        idWine: String,
        idWineComplement: String,
        wineHouse: Int
    ) {
        val batch: WriteBatch = getFireStore().batch()

        val wineHouseReference =
            getWinesCollection()
                .document(
                    idWine
                )

        batch.update(
            wineHouseReference,
            mapOf(
                "wineHouse" to wineHouse
            )
        )

        val dateWineHouseReference =
            getWinesComplementsCollection(
                idWine
            ).document(
                idWineComplement
            )

        val currentTime = DataUtil().getCurrentDateTime()

        batch.update(
            dateWineHouseReference,
            mapOf(
                "dateWineHouse" to currentTime
            )
        )

        batch.commit().addOnCompleteListener(
            executor,
            OnCompleteListener {
                if (it.isSuccessful) {
                    sqlite.updateWineHouse(idWine, wineHouse)
                    sqlite.updateDateWineHouse(currentTime, idWineComplement)
                }

                presenter.responseModifyWineHouse(
                    it,
                    wineHouse,
                    currentTime
                )
            }
        )
    }

    override fun saveBookmark(
        idWine: String,
        bookmark: Boolean
    ) {
        getWinesCollection()
            .document(idWine)
            .update(
                "bookmark",
                bookmark
            )
            .addOnCompleteListener(
                executor,
                OnCompleteListener {
                    if (it.isSuccessful) {
                        sqlite.updateBookmark(idWine, bookmark)
                    }

                    presenter.responseSaveBookmark(
                        it
                    )
                }
            )
    }

    override fun getFirestoreRecyclerOptionsComments(
        idWine: String
    ): FirestoreRecyclerOptions<Comment> {
        return FirestoreRecyclerOptions
            .Builder<Comment>()
            .setQuery(
                getWineCommentsCellection(
                    idWine
                ).orderBy(
                    "date",
                    Query.Direction.DESCENDING
                ),
                Comment::class.java
            ).build()
    }

    override fun getFirestoreRecyclerOptionsPurchases(
        idWine: String
    ): FirestoreRecyclerOptions<Purchase> {
        return FirestoreRecyclerOptions
            .Builder<Purchase>()
            .setQuery(
                getWinePurchasesCellection(
                    idWine
                ).orderBy(
                    "date",
                    Query.Direction.DESCENDING
                ),
                Purchase::class.java
            ).build()
    }

    override fun deleteWine(
        idWine: String,
        image: Map<String, Any>
    ) {
        getWinesCollection().document(
            idWine
        ).delete().addOnCompleteListener(
            executor,
            OnCompleteListener {
                if (it.isSuccessful) {
                    val nameImage: String = image[Wine.NameImage].toString()

                    if (nameImage.isNotEmpty()) {
                        Tasks.await(
                            getStorageCollection(
                                nameImage
                            ).delete().addOnCompleteListener(
                                executor,
                                OnCompleteListener { image ->
                                    if (!image.isSuccessful) {
                                        Log.i(
                                            "MAIN",
                                            "Erro ao deletar imagem!"
                                        )
                                    }
                                }
                            )
                        )
                    }

                    Tasks.await(
                        deleteCollection(
                            getWinesComplementsCollection(
                                idWine
                            )
                        )
                    )



                    Tasks.await(
                        deleteCollection(
                            getWinesComplementsCollection(
                                idWine
                            )
                        )
                    )

                    Tasks.await(
                        deleteCollection(
                            getWineCommentsCellection(
                                idWine
                            )
                        )
                    )
                    Tasks.await(
                        deleteCollection(
                            getWinePurchasesCellection(
                                idWine
                            )
                        )
                    )

                    sqlite.deleteWine(idWine)
                    sqlite.deleteWineComplement(idWine)
                    sqlite.deleteAllWineComment(idWine)
                    sqlite.deleteAllWinePurchases(idWine)

                    presenter.responseDelete(
                        it,
                        Wine.ParcelableWine
                    )
                }
            }
        )
    }

    override fun deleteComment(
        idWine: String,
        idComment: String
    ) {
        getWineCommentsCellection(
            idWine
        ).document(
            idComment
        ).delete().addOnCompleteListener(
            executor,
            OnCompleteListener {
                if (it.isSuccessful) {
                    sqlite.deleteComment(idComment).toString()
                }

                presenter.responseDelete(
                    it,
                    Comment.ParcelableComment
                )
            }
        )
    }

    override fun deletePurchase(
        idWine: String,
        idPurchse: String
    ) {
        getWinePurchasesCellection(
            idWine
        ).document(
            idPurchse
        ).delete().addOnCompleteListener(
            executor,
            OnCompleteListener {
                if (it.isSuccessful) {
                    sqlite.deletePurchase(idPurchse).toString()
                }

                presenter.responseDelete(
                    it,
                    Purchase.ParcelablePurchase
                )
            }
        )
    }

    private fun deleteCollection(
        collection: CollectionReference
    ): Task<Void> {
        val batchSize = 500
        return Tasks.call(executor, Callable<Void> {
            // Get the first batch of documents in the collection
            var query = collection.orderBy(FieldPath.documentId()).limit(batchSize.toLong())

            // Get a list of deleted documents
            var deleted = deleteQueryBatch(query)

            // While the deleted documents in the last batch indicate that there
            // may still be more documents in the collection, page down to the
            // next batch and delete again
            while (deleted.size >= batchSize) {
                // Move the query cursor to start after the last doc in the batch
                val last = deleted[deleted.size - 1]
                query = collection
                    .orderBy(
                        FieldPath.documentId()
                    )
                    .startAfter(
                        last.id
                    )
                    .limit(
                        batchSize.toLong()
                    )

                deleted = deleteQueryBatch(query)
            }

            null
        })
    }

    @WorkerThread
    @Throws(Exception::class)
    private fun deleteQueryBatch(query: Query): List<DocumentSnapshot> {
        val querySnapshot = Tasks.await(query.get())
        val batch = query.firestore.batch()
        for (snapshot in querySnapshot) {
            batch.delete(snapshot.reference)
        }
        Tasks.await(batch.commit())

        return querySnapshot.documents
    }
}