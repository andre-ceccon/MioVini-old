package vinho.andre.android.com.gerenciadorvinho.interfaces.data

import android.content.Context
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseUser
import vinho.andre.android.com.gerenciadorvinho.domain.Wine

interface MainActivityDataInterface {

    fun getFirebaseUser(): FirebaseUser

    fun prepareFirestoreRecyclerOptions(
        menuItemId: Int
    ): FirestoreRecyclerOptions<Wine>

    fun onSignOut(context: Context)
}