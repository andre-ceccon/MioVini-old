package vinho.andre.android.com.gerenciadorvinho.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

open class FirebaseBase {
    protected val executor = ThreadPoolExecutor(
        2, 4,
        60, TimeUnit.SECONDS, LinkedBlockingQueue()
    )

    protected fun getAuth(): FirebaseAuth =
        FirebaseAuth.getInstance()

    private fun getStorage(): FirebaseStorage =
        FirebaseStorage.getInstance()

    protected fun getFireStore(): FirebaseFirestore =
        FirebaseFirestore.getInstance()

    protected fun getWinesCollection(): CollectionReference {
        return getFireStore()
            .collection(
                "Wines"
            )
            .document(
                getAuth()
                    .currentUser!!
                    .uid
            )
            .collection(
                "Information"
            )
    }

    protected fun getWinesComplementsCollection(
        idWineComplement: String
    ): CollectionReference {
        return getWinesCollection()
            .document(
                idWineComplement
            )
            .collection(
                "Complement"
            )
    }

    protected fun getWineCommentsCellection(
        idWine: String
    ): CollectionReference {
        return getWinesCollection()
            .document(
                idWine
            ).collection(
                "Comments"
            )
    }

    protected fun getWinePurchasesCellection(
        idWine: String
    ): CollectionReference {
        return getWinesCollection()
            .document(
                idWine
            )
            .collection(
                "Purchases"
            )
    }

    protected fun getStorageCollection(
        imageName: String
    ): StorageReference {
        return getStorage()
            .reference
            .child(
                getAuth()
                    .currentUser!!
                    .uid
            )
            .child(
                imageName
            )
    }

    protected fun antigaGetUserCellection(): CollectionReference {
        return getFireStore()
            .collection("users")
            .document(
                getAuth().currentUser!!.uid
            )
            .collection("Infos")
    }
}