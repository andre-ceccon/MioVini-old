package vinho.andre.android.com.gerenciadorvinho.data.firebase.configuration

import com.google.firebase.auth.UserProfileChangeRequest
import vinho.andre.android.com.gerenciadorvinho.data.firebase.FirebaseBase
import vinho.andre.android.com.gerenciadorvinho.interfaces.data.configuration.ProfileDataInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.configuration.ProfilePresenteInterface

class ProfileData(
    val presenter: ProfilePresenteInterface
) : FirebaseBase(),
    ProfileDataInterface {

    override fun updateProfile(
        displayName: String
    ) {
        getAuth().currentUser?.updateProfile(
            UserProfileChangeRequest.Builder()
                .setDisplayName(
                    displayName
                ).build()
        )?.addOnCompleteListener {
            presenter.responseUpdate(
                it
            )
        }
    }
}