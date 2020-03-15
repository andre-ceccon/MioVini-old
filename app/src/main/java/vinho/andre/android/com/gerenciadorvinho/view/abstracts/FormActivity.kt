package vinho.andre.android.com.gerenciadorvinho.view.abstracts

import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.content_form.*
import kotlinx.android.synthetic.main.proxy_screen.*
import vinho.andre.android.com.gerenciadorvinho.R

abstract class FormActivity :
    AppCompatActivity(),
    TextView.OnEditorActionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        setSupportActionBar(toolbar)

        if (getLayoutResourceID() != R.layout.content_login) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        /*
        * Colocando a View de um arquivo XML como View filha
        * do item indicado no terceiro argumento.
        */
        View.inflate(
            this,
            getLayoutResourceID(),
            fl_form
        )
    }

    override fun onOptionsItemSelected(
        item: MenuItem
    ): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    abstract fun getLayoutResourceID(): Int

    /*
    * Caso o usuário toque no botão "Done" do teclado virtual
    * ao invés de tocar no botão "Entrar". Mesmo assim temos
    * de processar o formulário.
    * */
    override fun onEditorAction(view: TextView, actionId: Int, event: KeyEvent?): Boolean {
        mainAction()
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
    * Método template.
    * Responsável por conter o algoritmo de envio / validação
    * de dados. Algoritmo vinculado ao menos ao principal
    * botão em tela.
    * */
    fun mainAction(view: View? = null) {
        blockFields(true)
        isMainButtonSending(true)
        showProxy(true)
        requestActionOnFirebase()
    }

    /*
    * Necessário para que os campos de formulário não possam
    * ser acionados depois de enviados os dados.
    * */
    abstract fun blockFields(status: Boolean)

    /*
    * Necessário para que os campos de formulário possam
    * ser acionados caso seja preciso depois de enviados os dados.
    * */
    protected fun unBlockFields() {
        runOnUiThread {
            blockFields(false)
            isMainButtonSending(false)
            showProxy(false)
        }
    }

    /*
     * Muda o rótulo do botão principal de acordo com o status
     * do envio de dados.
     * */
    abstract fun isMainButtonSending(status: Boolean)

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

    abstract fun requestActionOnFirebase()
}