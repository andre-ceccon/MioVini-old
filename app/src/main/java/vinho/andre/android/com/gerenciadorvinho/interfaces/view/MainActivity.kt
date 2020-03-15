package vinho.andre.android.com.gerenciadorvinho.interfaces.view

import android.content.Context
import vinho.andre.android.com.gerenciadorvinho.domain.User
import vinho.andre.android.com.gerenciadorvinho.domain.Wine

interface MainActivity {
    fun setUserInformationInNavigation(
        user: User
    )

    fun getContext(): Context

    fun callLoginActivity()

    fun callDetailsActivity(
        wine: Wine
    )

    fun showProxy(
        status: Boolean
    )

    fun createDialogOfNoWineRegistration(
        title: String,
        menssage: String,
        textNegativeButton: String
    )

    fun updateToolbarSubTitle()
}