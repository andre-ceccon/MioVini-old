package vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.configuration

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser

interface EmailAndPasswordPresenterInterface {
    fun getUserLogged(): FirebaseUser

    fun reAuthenticateAndUpdateEmail(
        password: String
    )

    fun responseUpdateEmail(
        task: Task<Void>
    )

    fun responseUpdatePassword(
        task: Task<Void>
    )

    fun responseReAuthenticate(
        task: Task<Void>
    )
}