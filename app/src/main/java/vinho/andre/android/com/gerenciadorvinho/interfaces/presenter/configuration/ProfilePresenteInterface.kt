package vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.configuration

import com.google.android.gms.tasks.Task

interface ProfilePresenteInterface {
    fun updateProfile(
        displayName: String
    )

    fun responseUpdate(
        task: Task<Void>
    )
}