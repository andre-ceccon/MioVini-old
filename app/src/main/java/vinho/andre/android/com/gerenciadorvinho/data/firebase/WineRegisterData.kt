package vinho.andre.android.com.gerenciadorvinho.data.firebase

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.storage.UploadTask
import vinho.andre.android.com.gerenciadorvinho.data.sqlite.DBHelper
import vinho.andre.android.com.gerenciadorvinho.domain.Comment
import vinho.andre.android.com.gerenciadorvinho.domain.ImageUtil
import vinho.andre.android.com.gerenciadorvinho.domain.Purchase
import vinho.andre.android.com.gerenciadorvinho.domain.Wine
import vinho.andre.android.com.gerenciadorvinho.domain.WineComplement
import vinho.andre.android.com.gerenciadorvinho.interfaces.data.WineRegisterDataInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.WineRegisterPresenterInterface
import java.io.File

class WineRegisterData(
    private val context: Context,
    private val image: ImageUtil?,
    private val isUpdateOf: String?,
    private val presenter: WineRegisterPresenterInterface
) : FirebaseBase(),
    WineRegisterDataInterface {

    private val batch: WriteBatch =
        getFireStore().batch()

    private var comment: Comment? = null
    private var purchase: Purchase? = null

    private lateinit var wine: Wine
    private lateinit var wineComplement: WineComplement

    override fun onSaveWine(
        wine: Wine,
        comment: Comment,
        purchase: Purchase,
        wineComplement: WineComplement
    ) {
        onPrepareWine(
            wine
        )

        onPreparePurchase(
            purchase
        )

        onPrepareComment(
            comment
        )

        onPrepareWineComplement(
            wineComplement
        )

        onCommitBatch()
    }

    override fun onSaveWine(
        wine: Wine,
        purchase: Purchase,
        wineComplement: WineComplement
    ) {
        onPrepareWine(
            wine
        )

        onPreparePurchase(
            purchase
        )

        onPrepareWineComplement(
            wineComplement
        )

        onCommitBatch()
    }

    override fun onSaveWine(
        wine: Wine,
        comment: Comment,
        wineComplement: WineComplement
    ) {
        onPrepareWine(
            wine
        )

        onPrepareComment(
            comment
        )

        onPrepareWineComplement(
            wineComplement
        )

        onCommitBatch()
    }

    override fun onSaveWine(
        wine: Wine,
        wineComplement: WineComplement
    ) {
        onPrepareWine(
            wine
        )

        onPrepareWineComplement(
            wineComplement
        )

        onCommitBatch()
    }

    override fun onUploadImage() {
        val storageReference =
            getStorageCollection(
                image!!.nameImage
            )

        val uploadTask =
            storageReference
                .putFile(
                    Uri.fromFile(
                        File(
                            image.currentPathImage
                        )
                    )
                )

        uploadTask
            .addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                presenter.notifyProgressNotification(
                    progress.toInt()
                )
            }
            .continueWithTask(
                Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception
                            ?.let {
                                throw it
                            }
                    }
                    return@Continuation storageReference.downloadUrl
                }
            )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSaveInformationImage(
                        task.result.toString()
                    )
                } else {
                    saveOnSqlite()
                    callViewAction()
                    presenter.notifyUploadResult(false)
                }
            }
    }

    private fun onDeleteImage(
        nameImage: String
    ) {
        getStorageCollection(
            nameImage
        ).delete().addOnCompleteListener(
            executor,
            OnCompleteListener {
                if (it.isSuccessful) {
                    presenter.notifyUploadResult(true)
                } else {
                    Log.d(
                        "MAIN",
                        "Não Foi possivel deletar a Foto"
                    )
                }

                callViewAction(
                    it
                )
            }
        )
    }

    private fun onCommitBatch() {
        batch.commit().addOnCompleteListener(
            executor,
            OnCompleteListener {
                if (image != null) {
                    presenter.onSaveImage()
                } else {
                    saveOnSqlite()
                    callViewAction(it)
                }
            }
        )
    }

    private fun onSaveInformationImage(
        urlDownloadImage: String
    ) {
        val map = mapOf(
            Wine.NameImage to image!!.nameImage,
            Wine.UrlImage to urlDownloadImage
        )

        getWinesCollection()
            .document(
                wine.id
            )
            .update(
                Wine.FieldImage,
                map
            )
            .addOnCompleteListener(
                executor,
                OnCompleteListener {
                    if (it.isSuccessful) {
                        File(image.currentPathImage).delete()

                        wine.image = map

                        saveOnSqlite()

                        if (isUpdateOf != null &&
                            isUpdateOf == Wine.UpdateWine &&
                            image.nameOldImage.isNotBlank()
                        ) {
                            onDeleteImage(
                                image.nameOldImage
                            )
                        } else {
                            callViewAction(
                                it
                            )
                            presenter.notifyUploadResult(true)
                        }
                    } else {
                        onDeleteImage(
                            image.nameImage
                        )
                        presenter.notifyUploadResult(false)
                    }
                }
            )
    }

    private fun callViewAction(
        task: Task<Void>? = null
    ) {
        presenter.onWineRescueResponse(
            task,
            wine
        )
    }

    private fun onPrepareWine(
        wine: Wine
    ) {
        val wineReference: DocumentReference

        if (isUpdateOf == null) {
            wineReference =
                getWinesCollection().document()

            wine.id = wineReference.id

            batch.set(
                wineReference,
                wine.getMap()
            )
        } else if (isUpdateOf == Wine.UpdateWine) {
            wineReference =
                getWinesCollection()
                    .document(
                        wine.id
                    )

            batch.update(
                wineReference,
                wine.getMap()
            )
        }

        this.wine = wine
    }

    private fun onPrepareComment(
        comment: Comment
    ) {
        if (isUpdateOf != null && isUpdateOf == Comment.updateComment) {
            val commentReference =
                getWineCommentsCellection(
                    wine.id
                ).document(
                    comment.id
                )
            batch.update(commentReference, comment.getMap())
        } else {
            val commentReference =
                getWineCommentsCellection(
                    wine.id
                ).document()
            comment.id = commentReference.id
            batch.set(commentReference, comment.getMap())
        }

        this.comment = comment
    }

    private fun onPreparePurchase(
        purchase: Purchase
    ) {
        if (isUpdateOf != null && isUpdateOf == Purchase.updatePurchase) {
            val purchaseReference =
                getWinePurchasesCellection(
                    wine.id
                ).document(
                    purchase.id
                )
            batch.update(purchaseReference, purchase.getMap())
        } else {
            val purchaseReference =
                getWinePurchasesCellection(
                    wine.id
                ).document()
            purchase.id = purchaseReference.id
            batch.set(purchaseReference, purchase.getMap())
        }

        this.purchase = purchase
    }

    private fun onPrepareWineComplement(
        wineComplement: WineComplement
    ) {
        val wineComplementReference: DocumentReference

        if (isUpdateOf == null) {
            wineComplementReference =
                getWinesComplementsCollection(
                    wine.id
                ).document()

            wineComplement.id = wineComplementReference.id
            batch.set(wineComplementReference, wineComplement.getMap())
        } else if (isUpdateOf == Wine.UpdateWine) {
            wineComplementReference = getWinesComplementsCollection(
                wine.id
            ).document(
                wineComplement.id
            )
            batch.update(wineComplementReference, wineComplement.getMap())
        }

        this.wineComplement = wineComplement
    }

    private fun saveOnSqlite() {
        val sqlite = DBHelper(context)

        if (sqlite.hasWine(wine.id)) {
            sqlite.updateWine(wine)
            sqlite.updateWineComplement(
                wine.id,
                wineComplement
            )
        } else {
            sqlite.saveWine(wine)
            sqlite.saveWineComplement(
                wine.id,
                wineComplement
            )
        }

        if (purchase != null) {
            if (sqlite.hasPurchase(purchase!!.id)) {
                sqlite.updatePurchase(
                    wine.id,
                    purchase!!
                )
            } else {
                sqlite.savePurchase(
                    wine.id,
                    purchase!!
                )
            }
        }

        if (comment != null) {
            if (sqlite.hasComment(comment!!.id)) {
                sqlite.updateComment(
                    wine.id,
                    comment!!
                )
            } else {
                sqlite.saveComment(
                    wine.id,
                    comment!!
                )
            }
        }
    }
}