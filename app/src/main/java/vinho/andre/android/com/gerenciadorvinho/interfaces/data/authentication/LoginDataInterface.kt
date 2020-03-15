package vinho.andre.android.com.gerenciadorvinho.interfaces.data.authentication

import com.google.firebase.auth.AuthCredential

interface LoginDataInterface {

    fun isLoggedIn(): Boolean

    fun onLoginGoogle(credential: AuthCredential)

    fun onLoginEmailAndPassword(email: String, password: String)
}