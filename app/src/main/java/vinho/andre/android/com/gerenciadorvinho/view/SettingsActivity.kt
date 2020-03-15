package vinho.andre.android.com.gerenciadorvinho.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.content_settings.*
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.domain.User
import vinho.andre.android.com.gerenciadorvinho.view.adaprers.settings.SettingsListAdapter

class SettingsActivity :
    AppCompatActivity() {

    companion object {
        const val idProfileActivity = 1233
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        /*
        * Colocando em tela o usuário conectado.
        * */
        setUserName(getUser().name!!)

        initItems()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun getUser(): User = intent.getParcelableExtra(User.KEY)!!

    private fun setUserName(
        userName: String
    ) {
        tv_user_connected.text = String.format(
            "%s %s",
            getString(R.string.connected),
            userName
        )
    }

    /*
     * Método que inicializa a lista de itens de configurações
     * de conta.
     * */
    private fun initItems() {
        rv_account_settings_items.setHasFixedSize(false)

        val layoutManager = LinearLayoutManager(this)
        rv_account_settings_items.layoutManager = layoutManager

        val divider = DividerItemDecoration(
            this,
            layoutManager.orientation
        )

        divider.setDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.light_grey_divider_line
            )!!
        )

        rv_account_settings_items.addItemDecoration(divider)

        rv_account_settings_items.adapter =
            SettingsListAdapter(
                this
            )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == idProfileActivity && resultCode == Activity.RESULT_OK) {
            val user = getUser()
            val userName: String = data!!.getStringExtra(User.KEY)!!

            user.name = userName
            intent.putExtra(User.KEY, user)

            setUserName(
                data.getStringExtra(User.KEY)!!
            )
        }
    }
}