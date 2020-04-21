package vinho.andre.android.com.gerenciadorvinho.data.firebase.service

import android.content.Context
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.QuerySnapshot
import vinho.andre.android.com.gerenciadorvinho.data.firebase.FirebaseBase
import vinho.andre.android.com.gerenciadorvinho.data.sqlite.DBHelper
import vinho.andre.android.com.gerenciadorvinho.domain.Comment
import vinho.andre.android.com.gerenciadorvinho.domain.Purchase
import vinho.andre.android.com.gerenciadorvinho.domain.Wine
import vinho.andre.android.com.gerenciadorvinho.domain.WineComplement
import vinho.andre.android.com.gerenciadorvinho.interfaces.data.service.DataBaseSyncDataInterface
import vinho.andre.android.com.gerenciadorvinho.util.service.WineSynchronization

class DataBaseSyncData(
    val context: Context,
    private val job: WineSynchronization
) : FirebaseBase(),
    DataBaseSyncDataInterface {

    private val db = DBHelper(context)

    override fun syncWines() {
        getWinesCollection().get()
            .addOnCompleteListener(
                executor,
                OnCompleteListener { wine ->
                    if (wine.isSuccessful) {
                        if (wine.result != null) {
                            db.deleteAllWines()
                            db.deleteAllComments()
                            db.deleteAllPurchase()
                            db.deleteAllWineComplement()

                            wine.result!!.documents.forEach { doc ->
                                db.saveWine(
                                    doc.toObject(
                                        Wine::class.java
                                    )!!
                                )

                                var querySnapshot: QuerySnapshot = Tasks.await(
                                    getWinesComplementsCollection(
                                        doc.id
                                    ).get()
                                )

                                querySnapshot.forEach { Wc ->
                                    db.saveWineComplement(
                                        doc.id,
                                        Wc.toObject(
                                            WineComplement::class.java
                                        )
                                    )
                                }

                                querySnapshot = Tasks.await(
                                    getWinePurchasesCellection(
                                        doc.id
                                    ).get()
                                )

                                querySnapshot.forEach { purchase ->
                                    db.savePurchase(
                                        doc.id,
                                        purchase.toObject(
                                            Purchase::class.java
                                        )
                                    )
                                }

                                querySnapshot = Tasks.await(
                                    getWineCommentsCellection(
                                        doc.id
                                    ).get()
                                )

                                querySnapshot.forEach { comment ->
                                    db.saveComment(
                                        doc.id,
                                        comment.toObject(
                                            Comment::class.java
                                        )
                                    )
                                }
                            }

                            job.finishJob(job.getParams())
                        }
                    }
                }
            )
    }
}