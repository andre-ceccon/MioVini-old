package vinho.andre.android.com.gerenciadorvinho.view.configuration.connectiondata

import android.os.Bundle
import android.text.TextUtils
import kotlinx.android.synthetic.main.fragment_config_password.*
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.configuration.EmailAndPasswordPresenterInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.view.configuration.EmailAndPasswordInterface
import vinho.andre.android.com.gerenciadorvinho.presenter.configuration.FormEmailAndPasswordPresenter
import vinho.andre.android.com.gerenciadorvinho.util.function.isValidPassword
import vinho.andre.android.com.gerenciadorvinho.util.function.validate
import vinho.andre.android.com.gerenciadorvinho.view.abstracts.ConfigFormFragment

class FormPasswordFragment :
    ConfigFormFragment(),
    EmailAndPasswordInterface {

    private lateinit var presenter: EmailAndPasswordPresenterInterface

    override fun title() = R.string.config_connection_data_tab_password

    override fun getNewInformation(): String =
        et_new_password.text.toString().trim()

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

    override fun getLayoutResourceID() = R.layout.fragment_config_password

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bt_update_password.setOnClickListener {
            if (validateForm()) {
                callPasswordDialog()
            }
        }

        et_new_password.validate(
            { it.isValidPassword() },
            getString(R.string.invalid_password)
        )

        et_new_password_confirm.validate(
            {
                (et_new_password.text.isNotEmpty()
                        && it == et_new_password.text.toString())
                        || et_new_password.text.isEmpty()
            },
            getString(R.string.invalid_confirmed_password)
        )

        presenter =
            FormEmailAndPasswordPresenter(
                requireContext(),
                "password",
                this
            )

        et_new_password_confirm.setOnEditorActionListener(this)
    }

    override fun blockFields(status: Boolean) {
        et_new_password.isEnabled = !status
        et_new_password_confirm.isEnabled = !status
        bt_update_password.isEnabled = !status
    }

    override fun isMainButtonSending(status: Boolean) {
        bt_update_password.text =
            if (status)
                getString(R.string.update_password_going)
            else
                getString(R.string.update_password)
    }

    override fun requestActionOnFirebase(
        password: String
    ) = presenter.reAuthenticateAndUpdateEmail(
        password
    )

    override fun validateForm(): Boolean {
        if (et_new_password.error != null || et_new_password_confirm.error != null) {
            return false
        } else {
            when {
                TextUtils.isEmpty(et_new_password.text.toString()) -> {
                    et_new_password.error = getString(R.string.field_required)
                    return false
                }
                !et_new_password.text.toString().isValidPassword() -> {
                    et_new_password.error = getString(R.string.invalid_password)
                    return false
                }
                else -> et_new_password.error = null
            }

            when {
                TextUtils.isEmpty(et_new_password_confirm.text.toString()) -> {
                    et_new_password_confirm.error = getString(R.string.field_required)
                    return false
                }
                !et_new_password_confirm.text.toString().isValidPassword() -> {
                    et_new_password_confirm.error = getString(R.string.invalid_password)
                    return false
                }
                et_new_password.text.toString() != et_new_password_confirm.text.toString() -> {
                    et_new_password_confirm.error = getString(R.string.invalid_confirmed_password)
                    return false
                }
                else -> et_new_password_confirm.error = null
            }
            return true
        }
    }

    private fun clearFields() {
        et_new_password.setText("")
        et_new_password.error = null
        et_new_password_confirm.setText("")
        et_new_password_confirm.error = null
    }
}