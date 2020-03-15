package vinho.andre.android.com.gerenciadorvinho.view.abstracts

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_tabs_user_config.*
import vinho.andre.android.com.gerenciadorvinho.R

abstract class ConfigFormActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabs_user_config)
        setSupportActionBar(toolbar)

        /*
        * Para liberar o back button na barra de topo da
        * atividade.
        * */
        supportActionBar?.setDisplayHomeAsUpEnabled( true )
        supportActionBar?.setDisplayShowHomeEnabled( true )

        /*
         * Criando o adaptador de fragmentos que ficarão expostos
         * no ViewPager.
         * */
        val sectionsPagerAdapter = getSectionsAdapter()

        /*
         * Acessando o ViewPager e vinculando o adaptador de
         * fragmentos a ele.
         * */
        view_pager.adapter = sectionsPagerAdapter

        /*
         * Acessando o TabLayout e vinculando ele ao ViewPager
         * para que haja sincronia na escolha realizada em
         * qualquer um destes componentes visuais.
         * */
        tabs.setupWithViewPager( view_pager )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    abstract fun getSectionsAdapter() : FragmentPagerAdapter
}