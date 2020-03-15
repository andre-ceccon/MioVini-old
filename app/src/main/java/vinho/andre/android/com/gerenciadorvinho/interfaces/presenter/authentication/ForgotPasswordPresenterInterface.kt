package vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.authentication

import com.google.android.gms.tasks.Task

interface ForgotPasswordPresenterInterface {
    fun onSendRecoveryPassword(email: String)

    fun onResponseRecoveryPassword(result: Task<Void>)
}