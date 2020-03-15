package vinho.andre.android.com.gerenciadorvinho.presenter.authentication

import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.data.firebase.authentication.ForgotPasswordData
import vinho.andre.android.com.gerenciadorvinho.interfaces.data.authentication.ForgotPasswordDataInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.authentication.ForgotPasswordPresenterInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.view.ViewBaseInterface

class ForgotPasswordPresenter(
    private var view: ViewBaseInterface
) : ForgotPasswordPresenterInterface {

    private var data: ForgotPasswordDataInterface =
        ForgotPasswordData(this)

    override fun onSendRecoveryPassword(email: String) {
        data.onSendRecoveryPassword(email)
    }

    override fun onResponseRecoveryPassword(result: Task<Void>) {
        if (result.isSuccessful) {
            view.updateUI(null, R.string.email_sent)
        } else {
            try {
                throw result.exception!!
            } catch (e: FirebaseAuthInvalidUserException) {
                view.updateUI("Email", R.string.unregistered_email)
            } catch (e: FirebaseNetworkException) {
                view.updateUI(null, R.string.no_internet_connection)
            } catch (e: Exception) {
                view.updateUI(null, R.string.unable_to_login)
            }
        }
    }
}