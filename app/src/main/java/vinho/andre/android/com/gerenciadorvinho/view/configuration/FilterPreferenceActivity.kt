package vinho.andre.android.com.gerenciadorvinho.view.configuration

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.content_filter_preference.*
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.util.SharedPreferencesUtil

class FilterPreferenceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_preference)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
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

    override fun onStart() {
        super.onStart()

        val sp = SharedPreferencesUtil(this)

        when (sp.getAppOpenFilter()) {
            R.id.nav_in_the_cellar -> {
                configuracao.check(R.id.rb_adega)
            }
            R.id.nav_rating -> {
                configuracao.check(R.id.rb_classificacao)
            }
            R.id.nav_bookmark -> {
                configuracao.check(R.id.rb_favorito)
            }
            R.id.nav_name -> {
                configuracao.check(R.id.rb_nome)
            }
            R.id.nav_country_of_origin -> {
                configuracao.check(R.id.rb_origem)
            }
        }
    }

    fun onClickRadio(
        view: View? = null
    ) {
        val sp = SharedPreferencesUtil(this)

        when (configuracao.checkedRadioButtonId) {
            R.id.rb_adega -> {
                sp.saveAppOpenFilter(R.id.nav_in_the_cellar)
                snackBarFeedback(getString(R.string.field_wineHouse))
            }
            R.id.rb_classificacao -> {
                sp.saveAppOpenFilter(R.id.nav_rating)
                snackBarFeedback(getString(R.string.field_rating))
            }
            R.id.rb_favorito -> {
                sp.saveAppOpenFilter(R.id.nav_bookmark)
                snackBarFeedback(getString(R.string.field_bookmark))
            }
            R.id.rb_nome -> {
                sp.saveAppOpenFilter(R.id.nav_name)
                snackBarFeedback(getString(R.string.field_name))
            }
            R.id.rb_origem -> {
                sp.saveAppOpenFilter(R.id.nav_country_of_origin)
                snackBarFeedback(getString(R.string.field_country))
            }
        }
    }

    private fun snackBarFeedback(
        message: String
    ) {
        Snackbar.make(
            lay_conf,
            String.format(
                "%s %s",
                getString(R.string.defined_for),
                message
            ),
            Snackbar.LENGTH_LONG
        ).show()
    }
}