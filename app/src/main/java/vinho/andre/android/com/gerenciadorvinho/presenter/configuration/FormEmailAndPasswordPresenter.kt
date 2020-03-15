package vinho.andre.android.com.gerenciadorvinho.presenter.configuration

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.data.firebase.configuration.EmailAndPasswordData
import vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.configuration.EmailAndPasswordPresenterInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.view.configuration.EmailAndPasswordInterface

class FormEmailAndPasswordPresenter(
    private val context: Context,
    private val operation: String,
    private val view: EmailAndPasswordInterface
) : EmailAndPasswordPresenterInterface {

    private val data = EmailAndPasswordData(this)

    override fun getUserLogged(): FirebaseUser =
        data.getUserLogged()

    override fun reAuthenticateAndUpdateEmail(
        password: String
    ) = data.reAuthenticate(password)

    override fun responseUpdateEmail(
        task: Task<Void>
    ) {
        if (task.isSuccessful) {
            view.finishTask(context.getString(R.string.success_in_operation))
        } else {
            view.finishTask(context.getString(R.string.error_update))
        }
    }

    override fun responseUpdatePassword(
        task: Task<Void>
    ) {
        if (task.isSuccessful) {
            view.finishTask(context.getString(R.string.success_in_operation))
        } else {
            view.finishTask(context.getString(R.string.error_update))
        }
    }

    override fun responseReAuthenticate(
        task: Task<Void>
    ) {
        if (task.isSuccessful) {
            when (operation) {
                "email" -> {
                    data.updateEmail(
                        view.getNewInformation()
                    )
                }
                "password" -> {
                    data.updatePassword(
                        view.getNewInformation()
                    )
                }
            }
        } else {
            view.finishTask(context.getString(R.string.wrong_password))
        }
    }
}