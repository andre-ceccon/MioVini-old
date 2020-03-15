package vinho.andre.android.com.gerenciadorvinho.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ScreenUtils
import kotlinx.android.synthetic.main.content_form.*
import kotlinx.android.synthetic.main.content_sign_up.*
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.authentication.SignUpPresenterInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.view.ViewBaseInterface
import vinho.andre.android.com.gerenciadorvinho.presenter.authentication.SignUpPresenter
import vinho.andre.android.com.gerenciadorvinho.util.function.isValidEmail
import vinho.andre.android.com.gerenciadorvinho.util.function.isValidPassword
import vinho.andre.android.com.gerenciadorvinho.util.function.validate
import vinho.andre.android.com.gerenciadorvinho.view.abstracts.FormEmailAndPasswordActivity

class SignUpActivity :
    FormEmailAndPasswordActivity(),
    ViewBaseInterface {

    private lateinit var presenter: SignUpPresenterInterface

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

        /*
         * Colocando configuração de validação de campo de senha
         * para enquanto o usuário informa o conteúdo deste campo.
         * */
        et_password.validate(
            {
                it.isValidPassword()
            },
            getString(R.string.invalid_password)
        )

        /*
         * Colocando configuração de validação de campo de
         * confirmação de senha para enquanto o usuário informa o
         * conteúdo deste campo.
         * */
        et_confirm_password.validate(
            {
                /*
                 * O toString() em et_password.text.toString() é
                 * necessário, caso contrário a validação falha
                 * mesmo quando é para ser ok.
                 * */
                (et_password.text.isNotEmpty() && it == et_password.text.toString())
                        || et_password.text.isEmpty()
            },
            getString(R.string.invalid_confirmed_password)
        )

        et_confirm_password.setOnEditorActionListener(this)

        presenter = SignUpPresenter(this)
    }

    override fun getLayoutResourceID() = R.layout.content_sign_up

    override fun isConstraintToSiblingView(
        isKeyBoardOpened: Boolean
    ) =
        isKeyBoardOpened || ScreenUtils.isLandscape()

    override fun blockFields(
        status: Boolean
    ) {
        et_email.isEnabled = !status
        et_password.isEnabled = !status
        et_confirm_password.isEnabled = !status
        bt_sign_up.isEnabled = !status
    }

    override fun isMainButtonSending(
        status: Boolean
    ) {
        bt_sign_up.text =
            if (status)
                getString(R.string.sign_up_going)
            else
                getString(R.string.sign_up)
    }

    override fun setConstraintsRelativeToSiblingView(
        constraintSet: ConstraintSet,
        privacyId: Int
    ) {
        /*
         * Se o teclado virtual estiver aberto ou a tela
         * estiver em landscape, então mude a configuração
         * da View alvo (tv_privacy_policy) para ficar
         * vinculada a View acima dela (bt_sign_up).
         * */
        constraintSet.connect(
            privacyId,
            ConstraintLayout.LayoutParams.TOP,
            bt_sign_up.id,
            ConstraintLayout.LayoutParams.BOTTOM,
            (12 * ScreenUtils.getScreenDensity()).toInt()
        )
    }

    /* Listener de clique */
    fun callLoginActivity(view: View) {
        /*
         * Para evitar que tenhamos mais de uma
         * LoginActivity na pilha de atividades.
         * */
        if (ActivityUtils.isActivityExistsInStack(LoginActivity::class.java)) {
            finish()
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun requestActionOnFirebase() {
        if (validateForm()) {
            presenter.onSendSingUp(et_email.text.toString(), et_password.text.toString())
        } else {
            unBlockFields()
        }
    }

    private fun validateForm(): Boolean {
        if (et_email.error != null || et_password.error != null || et_confirm_password.error != null) {
            return false
        } else {
            if (TextUtils.isEmpty(et_email.text.toString())) {
                et_email.error = getString(R.string.field_required)
                return false
            } else {
                et_email.error = null
            }

            when {
                TextUtils.isEmpty(et_password.text.toString()) -> {
                    et_password.error = getString(R.string.field_required)
                    return false
                }
                et_password.text.length <= 7 -> {
                    et_password.error = getString(R.string.weak_password)
                    return false
                }
                else -> et_password.error = null
            }

            when {
                TextUtils.isEmpty(et_confirm_password.text.toString()) -> {
                    et_confirm_password.error = getString(R.string.field_required)
                    return false
                }
                et_confirm_password.text.toString() != et_password.text.toString() -> {
                    et_confirm_password.error = getString(R.string.invalid_confirmed_password)
                    return false
                }
                else -> et_confirm_password.error = null
            }
            return true
        }
    }

    override fun updateUI(
        campo: String?,
        idString: Int
    ) {
        unBlockFields()
        snackBarFeedback(
            fl_form_container,
            getString(idString)
        )

        when (campo) {
            "email" -> {
                et_email.error = getString(idString)
            }
            "password" -> {
                et_password.error = getString(idString)
            }
            "openMainActivity" -> {
                Handler().postDelayed({
                    startActivity(
                        Intent(
                            this,
                            MainActivity::class.java
                        )
                    )
                    finish()
                }, 500)
            }
        }
    }

    override fun getContext(): Context = this
}