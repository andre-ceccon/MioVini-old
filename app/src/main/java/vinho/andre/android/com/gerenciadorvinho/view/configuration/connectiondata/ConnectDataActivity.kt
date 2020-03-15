package vinho.andre.android.com.gerenciadorvinho.view.configuration.connectiondata

import vinho.andre.android.com.gerenciadorvinho.view.abstracts.ConfigFormActivity
import vinho.andre.android.com.gerenciadorvinho.view.adaprers.settings.ConfigSectionsAdapter

class ConnectDataActivity :
    ConfigFormActivity() {

    override fun getSectionsAdapter() = ConfigSectionsAdapter(
        this,
        supportFragmentManager,
        FormEmailFragment(),
        FormPasswordFragment()
    )
}