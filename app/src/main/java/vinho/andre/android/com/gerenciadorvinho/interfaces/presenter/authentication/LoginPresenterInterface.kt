package vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.authentication

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult

interface LoginPresenterInterface {
    fun getContext(): Context

    fun onSendLogin(credential: AuthCredential)

    fun onSendLogin(
        email: String,
        password: String
    )

    fun onResponseActivityResult(task: Task<GoogleSignInAccount>)

    fun onResponseRequestLogin(
        authResult: Task<AuthResult>
    )

    fun isLoggedIn()
}