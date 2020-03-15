package vinho.andre.android.com.gerenciadorvinho.interfaces.view

import android.content.Context

interface ViewBaseInterface {
    fun updateUI(
        campo: String?,
        idString: Int
    )

    fun getContext(): Context
}