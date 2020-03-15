package vinho.andre.android.com.gerenciadorvinho.view

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import kotlinx.android.synthetic.main.content_forgot_password.*
import kotlinx.android.synthetic.main.content_form.*
import kotlinx.android.synthetic.main.info_block.*
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.authentication.ForgotPasswordPresenterInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.view.ViewBaseInterface
import vinho.andre.android.com.gerenciadorvinho.presenter.authentication.ForgotPasswordPresenter
import vinho.andre.android.com.gerenciadorvinho.util.function.isValidEmail
import vinho.andre.android.com.gerenciadorvinho.util.function.validate
import vinho.andre.android.com.gerenciadorvinho.view.abstracts.FormActivity

class ForgotPasswordActivity :
    FormActivity(),
    ViewBaseInterface {

    private lateinit var presenter: ForgotPasswordPresenterInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
         * Colocando configuração de validação de campo de email
         * para enquanto o usuário informa o conteúdo deste campo.
         * */
        et_email.validate(
            {
                it.isValidEmail()
            },
            getString(R.string.invalid_email)
        )

        et_email.setOnEditorActionListener(this)

        tv_info_block.text = getString(R.string.forgot_password_info)

        presenter = ForgotPasswordPresenter(this)
    }

    override fun getLayoutResourceID() = R.layout.content_forgot_password

    override fun blockFields(status: Boolean) {
        et_email.isEnabled = !status
        bt_recover_password.isEnabled = !status
    }

    override fun isMainButtonSending(status: Boolean) {
        bt_recover_password.text =
            if (status)
                getString(R.string.recover_password_going)
            else
                getString(R.string.recover_password)
    }

    override fun requestActionOnFirebase() {
        if (validateForm()) {
            presenter.onSendRecoveryPassword(et_email.text.toString().trim())
        } else {
            unBlockFields()
        }
    }

    private fun validateForm(): Boolean {
        if (et_email.error != null) {
            return false
        } else {
            when {
                et_email.error != null -> return false
                TextUtils.isEmpty(et_email.text.toString()) -> {
                    et_email.error = getString(R.string.field_required)
                    return false
                }
                else -> et_email.error = null
            }
            return true
        }
    }

    override fun updateUI(campo: String?, idString: Int) {
        unBlockFields()
        when (campo) {
            "email" -> {
                et_email.error = getString(idString)
            }
        }

        snackBarFeedback(
            fl_form_container,
            getString(idString)
        )
    }

    override fun getContext(): Context = this
}