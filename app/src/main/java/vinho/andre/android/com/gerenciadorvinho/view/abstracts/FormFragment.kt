package vinho.andre.android.com.gerenciadorvinho.view.abstracts

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ColorUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_form.*
import kotlinx.android.synthetic.main.dialog_password.view.*
import kotlinx.android.synthetic.main.proxy_screen.*
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.util.function.isValidPassword
import vinho.andre.android.com.gerenciadorvinho.util.function.validate

abstract class FormFragment :
    Fragment(),
    TextView.OnEditorActionListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewContainer = inflater
            .inflate(
                R.layout.fragment_form,
                container,
                false
            ) as ViewGroup

        /*
         * Colocando a View de um arquivo XML como View filha
         * do item indicado no terceiro argumento.
         * */
        View.inflate(
            activity,
            getLayoutResourceID(),
            viewContainer.findViewById(R.id.fl_form)
        )

        return viewContainer
    }

    abstract fun getLayoutResourceID(): Int

    /*
     * Caso o usuário toque no botão "Done" do teclado virtual
     * ao invés de tocar no botão "Entrar". Mesmo assim temos
     * de processar o formulário.
     * */
    override fun onEditorAction(
        v: TextView?,
        actionId: Int,
        event: KeyEvent?
    ): Boolean {
        if (validateForm()) {
            callPasswordDialog()
        }
        return false
    }

    /*
     * Apresenta a tela de bloqueio que diz ao usuário que
     * algo está sendo processado em background e que ele
     * deve aguardar.
     * */
    protected fun showProxy(status: Boolean) {
        fl_proxy_container.visibility =
            if (status)
                View.VISIBLE
            else
                View.GONE
    }

    /*
     * Método responsável por apresentar um SnackBar com as
     * corretas configurações de acordo com o feedback do
     * back-end Web.
     * */
    protected fun snackBarFeedback(
        viewContainer: ViewGroup,
        message: String
    ) {
        Snackbar.make(
            viewContainer,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    /*
     * Método template.
     * Responsável por conter o algoritmo de envio / validação
     * de dados. Algoritmo vinculado ao menos ao principal
     * botão em tela.
     * */
    private fun mainAction(
        password: String
    ) {
        blockFields(true)
        isMainButtonSending(true)
        showProxy(true)
        requestActionOnFirebase(password)
    }

    /*
     * Necessário para que os campos de formulário não possam
     * ser acionados depois de enviados os dados.
     * */
    abstract fun blockFields(status: Boolean)

    protected fun unBlockFields() {
        blockFields(false)
        isMainButtonSending(false)
        showProxy(false)
    }

    /*
     * Muda o rótulo do botão principal de acordo com o status
     * do envio de dados.
     * */
    abstract fun isMainButtonSending(status: Boolean)

    abstract fun requestActionOnFirebase(
        password: String
    )

    abstract fun validateForm(): Boolean

    /*
     * Método responsável por invocar o Dialog de password antes
     * que o envio do formulário ocorra. Dialog necessário em
     * alguns formulários críticos onde parte da validação é a
     * verificação da senha.
     * */
    @SuppressLint("InflateParams")
    fun callPasswordDialog() {
        val builder = AlertDialog.Builder(activity!!)
        val inflater =
            activity!!.layoutInflater.inflate(
                R.layout.dialog_password,
                null
            )

        builder.setView(
            inflater
        ).setPositiveButton(
            R.string.dialog_password_go, null
        ).setNegativeButton(
            R.string.dialog_password_cancel
        ) { dialog, _ ->
            dialog.cancel()
        }.setCancelable(false)

        val dialog = builder.create()

        dialog.setOnShowListener {
            /*
             * É preciso colocar qualquer configuração
             * extra das Views do Dialog dentro do
             * listener de "dialog em apresentação",
             * caso contrário uma NullPointerException
             * será gerada, tendo em mente que é somente
             * quando o "dialog está em apresentação"
             * que as Views dele existem como objetos.
             */

            dialog.getButton(
                AlertDialog.BUTTON_POSITIVE
            ).setTextColor(ColorUtils.getColor(R.color.colorText))

            dialog.getButton(
                AlertDialog.BUTTON_NEGATIVE
            ).setTextColor(ColorUtils.getColor(R.color.colorText))

            val etPassword = dialog.findViewById<EditText>(R.id.et_password)!!
            etPassword.validate(
                { it.isValidPassword() },
                getString(R.string.invalid_password)
            )

            etPassword.setOnEditorActionListener { _, _, _ ->
                if (getPassword(inflater).isEmpty()) {
                    inflater.et_password.error = getString(R.string.field_required)
                } else if (!getPassword(inflater).isValidPassword()) {
                    inflater.et_password.error = getString(R.string.invalid_password)
                } else {
                    dialog.cancel()
                    mainAction(getPassword(inflater))
                }
                false
            }

            dialog.getButton(
                AlertDialog.BUTTON_POSITIVE
            ).setOnClickListener {
                if (getPassword(inflater).isEmpty()) {
                    inflater.et_password.error = getString(R.string.field_required)
                } else if (!getPassword(inflater).isValidPassword()) {
                    inflater.et_password.error = getString(R.string.invalid_password)
                } else {
                    dialog.cancel()
                    mainAction(getPassword(inflater))
                }
            }
        }
        dialog.show()
    }

    /*
     * Método necessário para atualizar o ViewGroup
     * fl_form, que é container dos layouts de formulários
     * carregados em fragment_form, deixando ele
     * pronto para receber uma lista de itens ou formulários
     * que têm os próprios padding e posicionamento.
     * */
    fun updateFlFormToFullFreeScreen() {
        fl_form.setPadding(0, 0, 0, 0)
        val layoutParams = (fl_form.layoutParams as FrameLayout.LayoutParams)
        layoutParams.gravity = Gravity.NO_GRAVITY
        layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT
        layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT
    }

    private fun getPassword(
        layoutDialog: View
    ): String =
        layoutDialog.et_password
            .text.toString()
            .trim()
}