package vinho.andre.android.com.gerenciadorvinho.view.abstracts

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.blankj.utilcode.util.KeyboardUtils
import kotlinx.android.synthetic.main.text_view_privacy_policy_login.*
import vinho.andre.android.com.gerenciadorvinho.view.PrivacyPolicyActivity

abstract class FormEmailAndPasswordActivity :
    FormActivity(),
    KeyboardUtils.OnSoftInputChangedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
         * Com a API KeyboardUtils conseguimos de maneira
         * simples obter o status atual do teclado virtual (aberto /
         * fechado) e assim prosseguir com algoritmos de ajuste de
         * layout.
         */
        KeyboardUtils.registerSoftInputChangedListener(this, this)
    }

    /*
     * Método idêntico.
     * */
    override fun onDestroy() {
        KeyboardUtils.unregisterSoftInputChangedListener(this)
        super.onDestroy()
    }

    /*
    * Método template.
   */
    override fun onSoftInputChanged(height: Int) {
        if (isAbleToCallChangePrivacyPolicyConstraints()) {
            changePrivacyPolicyConstraints(
                KeyboardUtils.isSoftInputVisible(this)
            )
        }
    }

    /*
     * Método gancho.
     */
    open fun isAbleToCallChangePrivacyPolicyConstraints() = true

    /*
     * Método template.
     */
    private fun changePrivacyPolicyConstraints(
        isKeyBoardOpened: Boolean
    ) {
        val privacyId = tv_privacy_policy.id
        val parent = tv_privacy_policy.parent as ConstraintLayout
        val constraintSet = ConstraintSet()

        /*
         * Definindo a largura e a altura da View em
         * mudança de constraints, caso contrário ela
         * fica com largura e altura em 0dp.
         * */
        constraintSet.constrainWidth(
            privacyId,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        constraintSet.constrainHeight(
            privacyId,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

        /*
         * Centralizando a View horizontalmente no
         * ConstraintLayout.
         * */
        constraintSet.centerHorizontally(
            privacyId,
            ConstraintLayout.LayoutParams.PARENT_ID
        )

        if (isConstraintToSiblingView(isKeyBoardOpened)) {
            setConstraintsRelativeToSiblingView(constraintSet, privacyId)
        } else {
            /*
             * Se o teclado virtual estiver fechado, então
             * mude a configuração da View alvo
             * (tv_privacy_policy) para ficar vinculada ao
             * fundo do ConstraintLayout ancestral.
             * */
            constraintSet.connect(
                privacyId,
                ConstraintLayout.LayoutParams.BOTTOM,
                ConstraintLayout.LayoutParams.PARENT_ID,
                ConstraintLayout.LayoutParams.BOTTOM
            )
        }

        constraintSet.applyTo(parent)
    }

    /*
     * Método único.
     * */
    abstract fun isConstraintToSiblingView(isKeyBoardOpened: Boolean): Boolean

    /*
     * Método único.
     * */
    abstract fun setConstraintsRelativeToSiblingView(
        constraintSet: ConstraintSet,
        privacyId: Int
    )

    /* Listeners de clique */
    /*
     * Método idêntico.
     * */
    fun callPrivacyPolicyFragment(
        view: View
    ) {
        startActivity(
            Intent(
                this,
                PrivacyPolicyActivity::class.java
            )
        )
    }
}