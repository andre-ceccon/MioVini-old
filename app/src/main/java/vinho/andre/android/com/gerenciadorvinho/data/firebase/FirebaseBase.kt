package vinho.andre.android.com.gerenciadorvinho.data.firebase

import android.content.Context
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import vinho.andre.android.com.gerenciadorvinho.domain.User
import vinho.andre.android.com.gerenciadorvinho.util.SharedPreferencesUtil
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

open class FirebaseBase {
    protected val executor = ThreadPoolExecutor(
        2, 4,
        60, TimeUnit.SECONDS, LinkedBlockingQueue()
    )

    protected fun getAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    private fun getStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    private fun getUserId(): String = getAuth().currentUser!!.uid

    protected fun getFireStore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    private fun getUidDocument(): DocumentReference {
        return getFireStore()
            .collection(
                "Wines"
            )
            .document(
                getUserId()
            )
    }

    protected fun getWinesCollection(): CollectionReference {
        return getUidDocument().collection(
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
                getUserId()
            )
            .child(
                imageName
            )
    }

    protected fun saveUidInDatabase(
        context: Context
    ) {
        getUidDocument().set(
            hashMapOf(
                "uId" to getUserId()
            )
        ).addOnCompleteListener(
            executor,
            OnCompleteListener {
                if (!it.isSuccessful) {
                    SharedPreferencesUtil(
                        context
                    ).saveShared(
                        User.ErrorUid,
                        User.ErrorUid
                    )
                }
            }
        )
    }
}