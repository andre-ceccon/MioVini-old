package vinho.andre.android.com.gerenciadorvinho.data.firebase.authentication

import com.google.firebase.auth.AuthCredential
import vinho.andre.android.com.gerenciadorvinho.data.firebase.FirebaseBase
import vinho.andre.android.com.gerenciadorvinho.interfaces.data.authentication.LoginDataInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.authentication.LoginPresenterInterface

class LoginData(
    private var loginpresenter: LoginPresenterInterface
) : FirebaseBase(), LoginDataInterface {

    override fun isLoggedIn(): Boolean {
        return getAuth().currentUser != null
    }

    override fun onLoginGoogle(
        credential: AuthCredential
    ) {
        getAuth().signInWithCredential(credential)
            .addOnCompleteListener { authResult ->
                saveUidInDatabase(loginpresenter.getContext())
                loginpresenter.onResponseRequestLogin(
                    authResult
                )
            }
    }

    override fun onLoginEmailAndPassword(
        email: String,
        password: String
    ) {
        getAuth()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { authResult ->
                loginpresenter.onResponseRequestLogin(
                    authResult
                )
            }
    }
}