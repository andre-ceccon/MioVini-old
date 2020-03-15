package vinho.andre.android.com.gerenciadorvinho.data.firebase.configuration

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import vinho.andre.android.com.gerenciadorvinho.data.firebase.FirebaseBase
import vinho.andre.android.com.gerenciadorvinho.interfaces.data.configuration.EmailDataInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.configuration.EmailAndPasswordPresenterInterface

class EmailAndPasswordData(
    private val presenter: EmailAndPasswordPresenterInterface
) : FirebaseBase(),
    EmailDataInterface {

    override fun getUserLogged(): FirebaseUser =
        getAuth().currentUser!!

    override fun updateEmail(
        newEmail: String
    ) {
        getUserLogged()
            .updateEmail(newEmail)
            .addOnCompleteListener {
                presenter.responseUpdateEmail(it)
            }
    }

    override fun updatePassword(
        newPassword: String
    ) {
        getUserLogged()
            .updatePassword(newPassword)
            .addOnCompleteListener {
                presenter.responseUpdatePassword(it)
            }
    }

    override fun reAuthenticate(
        password: String
    ) {
        val user = getUserLogged()
        user.reauthenticate(
            EmailAuthProvider.getCredential(
                user.email.toString().trim(),
                password.trim()
            )
        ).addOnCompleteListener {
            presenter.responseReAuthenticate(it)
        }
    }
}