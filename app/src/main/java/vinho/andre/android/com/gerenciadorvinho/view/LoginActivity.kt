package vinho.andre.android.com.gerenciadorvinho.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ScreenUtils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import kotlinx.android.synthetic.main.content_form.*
import kotlinx.android.synthetic.main.content_login.*
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.authentication.LoginPresenterInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.view.authentication.LoginViewInterface
import vinho.andre.android.com.gerenciadorvinho.presenter.authentication.LoginPresenter
import vinho.andre.android.com.gerenciadorvinho.util.function.isValidEmail
import vinho.andre.android.com.gerenciadorvinho.util.function.isValidPassword
import vinho.andre.android.com.gerenciadorvinho.util.function.validate
import vinho.andre.android.com.gerenciadorvinho.view.abstracts.FormEmailAndPasswordActivity

class LoginActivity :
    LoginViewInterface,
    FormEmailAndPasswordActivity() {

    private lateinit var presenter: LoginPresenterInterface
    private var signInGoogle: Int = 1001

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

        et_password.setOnEditorActionListener(this)

        google_button.setOnClickListener {
            signIn()
        }

        presenter = LoginPresenter(this)
    }

    override fun onStart() {
        super.onStart()
        reDesignGoogleButton(google_button, getString(R.string.sing_in_google))
        presenter.isLoggedIn()
    }

    override fun isAbleToCallChangePrivacyPolicyConstraints() = ScreenUtils.isPortrait()

    override fun isConstraintToSiblingView(
        isKeyBoardOpened: Boolean
    ) = isKeyBoardOpened

    override fun setConstraintsRelativeToSiblingView(
        constraintSet: ConstraintSet,
        privacyId: Int
    ) {
        /*
         * Se o teclado virtual estiver aberto, então
         * mude a configuração da View alvo
         * (tv_privacy_policy) para ficar vinculada a
         * View acima dela (tv_sign_up).
         * */
        constraintSet.connect(
            privacyId,
            ConstraintLayout.LayoutParams.TOP,
            tv_sign_up.id,
            ConstraintLayout.LayoutParams.BOTTOM,
            (12 * ScreenUtils.getScreenDensity()).toInt()
        )
    }

    private fun signIn() {
        startActivityForResult(
            GoogleSignIn.getClient(
                this,
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            ).signInIntent,
            signInGoogle
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == signInGoogle) {
            presenter.onResponseActivityResult(
                GoogleSignIn.getSignedInAccountFromIntent(data)
            )
        }
    }

    /* Métodos da FormActivity() */
    override fun getLayoutResourceID() = R.layout.content_login

    override fun blockFields(
        status: Boolean
    ) {
        et_email.isEnabled = !status
        et_password.isEnabled = !status
        bt_login.isEnabled = !status
    }

    override fun isMainButtonSending(
        status: Boolean
    ) {
        bt_login.text =
            if (status)
                getString(R.string.sign_in_going)
            else
                getString(R.string.sign_in)
    }

    /*
    * Antes de chamar a ação no FirebaseBase,
    * É verificado se o email ou senha é nulo.
    * Atraves do método validateForm()
    */
    override fun requestActionOnFirebase() {
        if (validateForm()) {
            presenter.onSendLogin(et_email.text.toString().trim(), et_password.text.toString().trim())
        } else {
            unBlockFields()
        }
    }

    /* Listeners de clique, setados no XLM */
    fun callForgotPasswordActivity(
        view: View
    ) {
        /*
         * Para evitar que tenhamos mais de uma
         * SignUpActivity na pilha de atividades.
         * */
        if (ActivityUtils.isActivityExistsInStack(ForgotPasswordActivity::class.java)) {
            finish()
        } else {
            startActivity(
                Intent(
                    this,
                    ForgotPasswordActivity::class.java
                )
            )
        }
        clearFields()
    }

    fun callSignUpActivity(
        view: View
    ) {
        /*
         * Para evitar que tenhamos mais de uma
         * SignUpActivity na pilha de atividades.
         * */
        if (ActivityUtils.isActivityExistsInStack(SignUpActivity::class.java)) {
            finish()
        } else {
            startActivity(
                Intent(
                    this,
                    SignUpActivity::class.java
                )
            )
        }
        clearFields()
    }

    /* Métodos da LoginViewInterface */
    private fun validateForm(): Boolean {
        if (et_email.error != null || et_password.error != null) {
            return false
        } else {
            if (TextUtils.isEmpty(et_email.text.toString())) {
                et_email.error = getString(R.string.field_required)
                return false
            } else {
                et_email.error = null
            }

            if (TextUtils.isEmpty(et_password.text.toString())) {
                et_password.error = getString(R.string.field_required)
                return false
            } else {
                et_password.error = null
            }
            return true
        }
    }

    override fun callMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun updateUI(
        campo: String?,
        idString: Int
    ) {
        unBlockFields()
        when (campo) {
            "Email" -> {
                et_email.error = getString(idString)
            }
            "Senha" -> {
                et_password.error = getString(idString)
            }
        }

        snackBarFeedback(
            fl_form_container,
            getString(idString)
        )
    }

    private fun reDesignGoogleButton(
        signInButton: SignInButton,
        buttonText: String
    ) {
        for (i in 0 until signInButton.childCount) {
            val v = signInButton.getChildAt(i)
            if (v is TextView) {
                v.text = buttonText
                return
            }
        }
    }

    private fun clearFields() {
        et_email.text = null
        et_password.text = null
        et_email.error = null
        et_password.error = null
    }

    override fun getContext(): Context = this
}