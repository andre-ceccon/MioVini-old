package vinho.andre.android.com.gerenciadorvinho.interfaces.data.configuration

import com.google.firebase.auth.FirebaseUser

interface EmailDataInterface {
    fun getUserLogged(): FirebaseUser

    fun updateEmail(
        newEmail: String
    )

    fun updatePassword(
        newPassword: String
    )

    fun reAuthenticate(
        password: String
    )
}