package vinho.andre.android.com.gerenciadorvinho.presenter.authentication

import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.data.firebase.authentication.SignUpData
import vinho.andre.android.com.gerenciadorvinho.interfaces.data.authentication.SignUpDataInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.authentication.SignUpPresenterInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.view.ViewBaseInterface
import vinho.andre.android.com.gerenciadorvinho.util.SharedPreferencesUtil

class SignUpPresenter(
    private var view: ViewBaseInterface
) : SignUpPresenterInterface {

    private var model: SignUpDataInterface =
        SignUpData(this)

    override fun onSendSingUp(
        email: String,
        password: String
    ) = model.onSendSingUp(email, password)

    override fun onResponseRequestSignUp(
        result: Task<AuthResult>
    ) {
        if (result.isSuccessful) {
            SharedPreferencesUtil(
                view.getContext()
            ).saveIsNewUser(result.result?.additionalUserInfo?.isNewUser)

            view.updateUI(
                "openMainActivity",
                R.string.account_successfully_created
            )
        } else {
            try {
                throw result.exception!!
            } catch (exception: FirebaseAuthWeakPasswordException) {
                view.updateUI("password", R.string.weak_password)
            } catch (exception: FirebaseAuthInvalidCredentialsException) {
                view.updateUI("email", R.string.invalid_email)
            } catch (exception: FirebaseAuthUserCollisionException) {
                view.updateUI("email", R.string.invalid_sign_up_email)
            } catch (e: FirebaseNetworkException) {
                view.updateUI(null, R.string.no_internet_connection)
            } catch (exception: Exception) {
                view.updateUI(null, R.string.could_not_register)
            }
        }
    }
}