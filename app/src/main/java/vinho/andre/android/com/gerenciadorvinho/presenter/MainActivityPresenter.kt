package vinho.andre.android.com.gerenciadorvinho.presenter

import android.content.Context
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import vinho.andre.android.com.gerenciadorvinho.data.firebase.MainActivityData
import vinho.andre.android.com.gerenciadorvinho.domain.User
import vinho.andre.android.com.gerenciadorvinho.domain.Wine
import vinho.andre.android.com.gerenciadorvinho.interfaces.data.MainActivityDataInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.MainActivityPresenterInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.view.MainActivity

class MainActivityPresenter(
    private var view: MainActivity
) : MainActivityPresenterInterface {

    private var model: MainActivityDataInterface =
        MainActivityData(this)

    override fun callLoginActivity() =
        view.callLoginActivity()

    override fun signOut(
        context: Context
    ) = model.onSignOut(context)

    override fun getUser(): User {
        val user = model.getFirebaseUser()
        return User(
            user.uid,
            if (user.displayName.isNullOrEmpty()) {
                user.email?.substringBefore("@") ?: ""
            } else {
                user.displayName
            },
            user.email,
            user.photoUrl,
            user.providerData[1].providerId
        )
    }

    override fun getUserInformation() {
        model.getFirebaseUser()
            .reload()
            .addOnCompleteListener {
                val user = model.getFirebaseUser()
                view.setUserInformationInNavigation(
                    User(
                        user.uid,
                        if (user.displayName.isNullOrEmpty()) {
                            user.email?.substringBefore("@") ?: ""
                        } else {
                            user.displayName
                        },
                        user.email,
                        user.photoUrl,
                        user.providerData[1].providerId
                    )
                )
            }
    }

    override fun getFirestoreRecyclerOptions(
        menuItemId: Int
    ): FirestoreRecyclerOptions<Wine> =
        model.prepareFirestoreRecyclerOptions(menuItemId)
}