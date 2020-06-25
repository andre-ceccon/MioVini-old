package vinho.andre.android.com.gerenciadorvinho.data.firebase.authentication

import vinho.andre.android.com.gerenciadorvinho.data.firebase.FirebaseBase
import vinho.andre.android.com.gerenciadorvinho.interfaces.data.authentication.SignUpDataInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.authentication.SignUpPresenterInterface

class SignUpData(
    private var presenter: SignUpPresenterInterface
) : FirebaseBase(),
    SignUpDataInterface {

    override fun onSendSingUp(email: String, password: String) {
        getAuth().createUserWithEmailAndPassword(
            email, password
        ).addOnCompleteListener { authResult ->
            saveUidInDatabase(presenter.getContext())
            presenter.onResponseRequestSignUp(authResult)
        }
    }
}