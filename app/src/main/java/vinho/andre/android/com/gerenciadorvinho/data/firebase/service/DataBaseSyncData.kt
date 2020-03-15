package vinho.andre.android.com.gerenciadorvinho.data.firebase.service

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import vinho.andre.android.com.gerenciadorvinho.data.firebase.FirebaseBase
import vinho.andre.android.com.gerenciadorvinho.data.sqlite.DBHelper
import vinho.andre.android.com.gerenciadorvinho.domain.Comment
import vinho.andre.android.com.gerenciadorvinho.domain.Purchase
import vinho.andre.android.com.gerenciadorvinho.domain.Wine
import vinho.andre.android.com.gerenciadorvinho.domain.Wine2
import vinho.andre.android.com.gerenciadorvinho.domain.WineComplement
import vinho.andre.android.com.gerenciadorvinho.interfaces.data.service.DataBaseSyncDataInterface
import vinho.andre.android.com.gerenciadorvinho.util.DataUtil
import vinho.andre.android.com.gerenciadorvinho.util.service.WineSynchronization

class DataBaseSyncData(
    val context: Context,
    private val job: WineSynchronization
) : FirebaseBase(),
    DataBaseSyncDataInterface {

    private val dataUtil = DataUtil()
    private val db = DBHelper(context)

    override fun syncWines() {
        getWinesCollection().get().addOnCompleteListener(
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

    override fun reRegister() {
        antigaGetUserCellection()
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    it.result
                        ?.documents
                        ?.onEach { documentSnapshot ->
                            val oldWine: Wine2 =
                                documentSnapshot.toObject(
                                    Wine2::class.java
                                )!!

                            oldWine.id = documentSnapshot.id

                            toObject(oldWine)

                            purchase(documentSnapshot.id)
                        }
                }
            }
    }

    private fun toObject(
        oldWine: Wine2?
    ) {
        val batch = getFireStore().batch()

        val wine = Wine(
            oldWine?.id ?: "",
            oldWine?.nome ?: "",
            oldWine?.origem ?: "",
            mapOf(
                "name" to oldWine?.pathimgcelular!!.replace(
                    "/storage/emulated/0/MioVini/Fotos/",
                    ""
                ),
                "url" to ""
            ),
            oldWine.safra,
            oldWine.adega,
            oldWine.favorito,
            oldWine.avaliacao
        )

        val wineRef = getWinesCollection().document(wine.id)
        batch.set(wineRef, wine.getMap())

        val wineComplementRef = getWinesComplementsCollection(
            wine.id
        ).document()
        val wineComplement = WineComplement(
            wineComplementRef.id,
            oldWine.uva,
            oldWine.harmonizacao,
            oldWine.temp,
            oldWine.produtor,
            dataUtil.getCurrentDateTime()
        )

        batch.set(wineComplementRef, wineComplement.getMap())

        if (oldWine.comentario.isNotEmpty()) {
            val sfRef = getWineCommentsCellection(
                wine.id
            ).document()

            batch.set(
                sfRef, mapOf(
                    "id" to sfRef.id,
                    "date" to dataUtil.getCurrentDateTime(),
                    "comment" to oldWine.comentario
                )
            )
        }

        batch.commit().addOnCompleteListener {
            Log.i("MAIN", "Wine: ${wine.name} ${it.isSuccessful}")
        }
    }

    private fun purchase(
        idWine: String
    ) {
        antigaGetUserCellection()
            .document(
                idWine
            )
            .collection(
                "Precos"
            )
            .orderBy(
                "data",
                Query.Direction.ASCENDING
            )
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    it.result
                        ?.documents
                        ?.onEach { documentSnapshot ->
                            if (documentSnapshot != null) {
                                val compra = Purchase(
                                    documentSnapshot.getLong("safra")!!.toInt(),
                                    documentSnapshot.getLong("quantidade")!!.toInt(),
                                    documentSnapshot.getDouble("preco")!!,
                                    documentSnapshot.getDate("data")!!,
                                    documentSnapshot.id,
                                    documentSnapshot.getString("loja")!!,
                                    documentSnapshot.getString("comentario")!!
                                )

                                getWinesCollection()
                                    .document(idWine)
                                    .update("vintage", compra.vintage)
                                    .addOnCompleteListener { aa ->
                                        Log.i(
                                            "MAIN",
                                            "Vintage: $idWine ${compra.vintage} ${aa.isSuccessful}"
                                        )
                                    }

                                getWinePurchasesCellection(
                                    idWine
                                ).document(
                                    documentSnapshot.id
                                ).set(
                                    compra.getMap()
                                ).addOnCompleteListener { itSet ->
                                    Log.i("MAIN", "Purchase: $idWine ${itSet.isSuccessful}")
                                }
                            }
                        }
                }
            }
    }
}