package vinho.andre.android.com.gerenciadorvinho.interfaces.presenter

import android.content.Context
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import vinho.andre.android.com.gerenciadorvinho.domain.User
import vinho.andre.android.com.gerenciadorvinho.domain.Wine

interface MainActivityPresenterInterface {
    fun getUser(): User

    fun getUserInformation()

    fun getFirestoreRecyclerOptions(
        menuItemId: Int
    ): FirestoreRecyclerOptions<Wine>

    fun signOut(
        context: Context
    )

    fun callLoginActivity()
}