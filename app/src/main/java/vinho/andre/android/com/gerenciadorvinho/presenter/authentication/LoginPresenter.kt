package vinho.andre.android.com.gerenciadorvinho.presenter.authentication

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.data.firebase.authentication.LoginData
import vinho.andre.android.com.gerenciadorvinho.interfaces.data.authentication.LoginDataInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.authentication.LoginPresenterInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.view.authentication.LoginViewInterface

class LoginPresenter(
    var view: LoginViewInterface
) : LoginPresenterInterface {

    private var model: LoginDataInterface =
        LoginData(this)

    override fun getContext(): Context = view.getContext()

    override fun isLoggedIn() {
        if (model.isLoggedIn()) {
            view.callMainActivity()
        }
    }

    override fun onSendLogin(
        credential: AuthCredential
    ) = model.onLoginGoogle(credential)

    override fun onSendLogin(
        email: String,
        password: String
    ) = model.onLoginEmailAndPassword(email, password)

    override fun onResponseRequestLogin(
        authResult: Task<AuthResult>
    ) {
        if (authResult.isSuccessful) {
            view.callMainActivity()
        } else {
            try {
                throw authResult.exception!!
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                when (e.errorCode) {
                    "ERROR_WRONG_PASSWORD" -> {
                        view.updateUI("Senha", R.string.wrong_password)
                    }
                }
            } catch (e: FirebaseAuthInvalidUserException) {
                view.updateUI("Email", R.string.unregistered_email)
            } catch (e: FirebaseNetworkException) {
                view.updateUI(null, R.string.no_internet_connection)
            } catch (e: Exception) {
                view.updateUI(null, R.string.unable_to_login)
            }
        }
    }

    override fun onResponseActivityResult(task: Task<GoogleSignInAccount>) {
        try {
            onSendLogin(
                GoogleAuthProvider.getCredential(
                    task.getResult(ApiException::class.java)!!.idToken,
                    null
                )
            )
        } catch (e: Exception) {
            view.updateUI(null, R.string.unable_to_login)
        }
    }
}