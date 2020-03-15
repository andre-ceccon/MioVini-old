package vinho.andre.android.com.gerenciadorvinho.data.firebase.authentication

import vinho.andre.android.com.gerenciadorvinho.data.firebase.FirebaseBase
import vinho.andre.android.com.gerenciadorvinho.interfaces.data.authentication.ForgotPasswordDataInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.authentication.ForgotPasswordPresenterInterface

class ForgotPasswordData(
    private var presenter: ForgotPasswordPresenterInterface
) : FirebaseBase(),
    ForgotPasswordDataInterface {

    override fun onSendRecoveryPassword(email: String) {
        getAuth().sendPasswordResetEmail(email)
            .addOnCompleteListener {
                presenter.onResponseRecoveryPassword(it)
            }
    }
}