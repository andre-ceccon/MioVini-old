package vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.authentication

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

interface SignUpPresenterInterface {
    fun onSendSingUp(email: String, password: String)

    fun onResponseRequestSignUp(result: Task<AuthResult>)
}