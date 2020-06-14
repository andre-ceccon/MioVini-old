package vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.authentication

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

interface SignUpPresenterInterface {
    fun getContext(): Context

    fun onSendSingUp(
        email: String,
        password: String
    )

    fun onResponseRequestSignUp(
        authResult: Task<AuthResult>
    )
}