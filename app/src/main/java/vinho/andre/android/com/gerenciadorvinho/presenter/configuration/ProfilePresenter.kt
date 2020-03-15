package vinho.andre.android.com.gerenciadorvinho.presenter.configuration

import com.google.android.gms.tasks.Task
import vinho.andre.android.com.gerenciadorvinho.data.firebase.configuration.ProfileData
import vinho.andre.android.com.gerenciadorvinho.interfaces.data.configuration.ProfileDataInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.configuration.ProfilePresenteInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.view.configuration.ProfileActivityInterface

class ProfilePresenter(
    val view: ProfileActivityInterface
) : ProfilePresenteInterface {

    private val data: ProfileDataInterface =
        ProfileData(this)

    override fun updateProfile(
        displayName: String
    ) {
        data.updateProfile(
            displayName
        )
    }

    override fun responseUpdate(
        task: Task<Void>
    ) {
        view.responseUpdate(task.isSuccessful)
    }
}