package vinho.andre.android.com.gerenciadorvinho.view.configuration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.NetworkUtils
import kotlinx.android.synthetic.main.content_form.*
import kotlinx.android.synthetic.main.content_profile.*
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.domain.User
import vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.configuration.ProfilePresenteInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.view.configuration.ProfileActivityInterface
import vinho.andre.android.com.gerenciadorvinho.presenter.configuration.ProfilePresenter
import vinho.andre.android.com.gerenciadorvinho.util.function.validate
import vinho.andre.android.com.gerenciadorvinho.view.abstracts.FormActivity

class ProfileActivity :
    FormActivity(),
    ProfileActivityInterface {

    private var updateName: Boolean = false
    private lateinit var presenter: ProfilePresenteInterface

    private fun getUser(): User = intent.getParcelableExtra(User.KEY)!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        et_name.validate(
            {
                it.length > 1
            },
            getString(R.string.invalid_name)
        )
        et_name.setOnEditorActionListener(this)

        /*
         * Name é um dos dados de banco de dados, e campo de
         * formulário, que nunca poderá estar vázio.
         * */
        et_name.setText(getUser().name!!)

        presenter = ProfilePresenter(this)
    }

    override fun onBackPressed() {
        setResult(
            Activity.RESULT_OK,
            Intent().putExtra(
                User.KEY,
                if (updateName) {
                    et_name.text.toString()
                } else {
                    getUser().name!!
                }

            )
        )
        super.onBackPressed()
    }

    override fun getLayoutResourceID(): Int = R.layout.content_profile

    override fun blockFields(status: Boolean) {
        et_name.isEnabled = !status
        bt_send_profile.isEnabled = !status
    }

    override fun isMainButtonSending(status: Boolean) {
        bt_send_profile.text =
            if (status)
                getString(R.string.config_profile_going)
            else
                getString(R.string.config_profile)
    }

    override fun requestActionOnFirebase() {
        if (et_name.text.toString().isNotEmpty()) {
            if (NetworkUtils.isConnected()) {
                presenter.updateProfile(
                    et_name.text.toString().trim()
                )
            } else {
                unBlockFields()
                snackBarFeedback(
                    fl_form_container,
                    getString(R.string.no_internet_connection)
                )
            }
        } else {
            et_name.error = getString(R.string.field_required)
            unBlockFields()
        }
    }

    override fun responseUpdate(
        status: Boolean
    ) {
        unBlockFields()
        snackBarFeedback(
            fl_form_container,
            if (status) {
                updateName = true
                getString(R.string.success_in_operation)
            } else {
                getString(R.string.error_saving)
            }
        )
    }
}