package vinho.andre.android.com.gerenciadorvinho.view.configuration.connectiondata

import android.os.Bundle
import android.text.TextUtils
import kotlinx.android.synthetic.main.fragment_config_email.*
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.configuration.EmailAndPasswordPresenterInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.view.configuration.EmailAndPasswordInterface
import vinho.andre.android.com.gerenciadorvinho.presenter.configuration.FormEmailAndPasswordPresenter
import vinho.andre.android.com.gerenciadorvinho.util.function.isValidEmail
import vinho.andre.android.com.gerenciadorvinho.util.function.validate
import vinho.andre.android.com.gerenciadorvinho.view.abstracts.ConfigFormFragment

class FormEmailFragment :
    EmailAndPasswordInterface,
    ConfigFormFragment() {

    private lateinit var presenter: EmailAndPasswordPresenterInterface

    override fun title() = R.string.config_connection_data_tab_email

    override fun getLayoutResourceID() =
        R.layout.fragment_config_email

    override fun getNewInformation(): String =
        et_new_email.text.toString().trim()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bt_update_email_login.setOnClickListener {
            if (validateForm()) {
                callPasswordDialog()
            }
        }

        et_current_email.validate(
            {
                it.isValidEmail()
            },
            getString(R.string.invalid_email)
        )

        et_new_email.validate(
            {
                it.isValidEmail()
            },
            getString(R.string.invalid_email)
        )

        et_new_email_confirm.validate(
            {
                (et_new_email.text.isNotEmpty()
                        && it == et_new_email.text.toString())
                        || et_new_email.text.isEmpty()
            },
            getString(R.string.invalid_confirmed_email)
        )

        presenter =
            FormEmailAndPasswordPresenter(
                requireContext(),
                "email",
                this
            )

        et_new_email_confirm.setOnEditorActionListener(this)
    }

    override fun blockFields(status: Boolean) {
        et_current_email.isEnabled = !status
        et_new_email.isEnabled = !status
        et_new_email_confirm.isEnabled = !status
        bt_update_email_login.isEnabled = !status
    }

    override fun isMainButtonSending(status: Boolean) {
        bt_update_email_login.text =
            if (status)
                getString(R.string.update_email_login_going)
            else
                getString(R.string.update_email_login)
    }

    override fun requestActionOnFirebase(
        password: String
    ) = presenter.reAuthenticateAndUpdateEmail(
        password
    )

    override fun finishTask(
        message: String
    ) {
        unBlockFields()
        snackBarFeedback(
            ll_container_fields,
            message
        )

        if (message.contentEquals(getString(R.string.success_in_operation))) {
            clearFields()
        }
    }

    override fun validateForm(): Boolean {
        if (et_current_email.error != null || et_new_email.error != null || et_new_email_confirm.error != null) {
            return false
        } else {
            when {
                TextUtils.isEmpty(et_current_email.text.toString()) -> {
                    et_current_email.error = getString(R.string.field_required)
                    return false
                }
                presenter.getUserLogged().email != et_current_email.text.toString() -> {
                    et_current_email.error = "Email diferente do atual"
                    return false
                }
                else -> {
                    et_current_email.error = null
                }
            }

            when {
                TextUtils.isEmpty(et_new_email.text.toString()) -> {
                    et_new_email.error = getString(R.string.field_required)
                    return false
                }
                !et_new_email.text.toString().isValidEmail() -> {
                    et_new_email.error = getString(R.string.invalid_email)
                    return false
                }
                else -> et_new_email.error = null
            }

            when {
                TextUtils.isEmpty(et_new_email_confirm.text.toString()) -> {
                    et_new_email_confirm.error = getString(R.string.field_required)
                    return false
                }
                !et_new_email_confirm.text.toString().isValidEmail() -> {
                    et_new_email.error = getString(R.string.invalid_email)
                    return false
                }
                et_new_email.text.toString() != et_new_email_confirm.text.toString() -> {
                    et_new_email_confirm.error = getString(R.string.invalid_confirmed_email)
                    return false
                }
                else -> et_new_email_confirm.error = null
            }
            return true
        }
    }

    private fun clearFields() {
        et_current_email.setText("")
        et_current_email.error = null
        et_new_email.setText("")
        et_new_email.error = null
        et_new_email_confirm.setText("")
    }
}