package vinho.andre.android.com.gerenciadorvinho.view.adaprers.settings

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import vinho.andre.android.com.gerenciadorvinho.view.abstracts.ConfigFormFragment

/**
 * Um FragmentPagerAdapter que retorna um fragmento correspondendo
 * a uma das sections/tabs/pages.
 *
 * Mesmo que o método getItem() indique que mais de uma instância
 * do mesmo fragmento será criada, na verdade objetos
 * FragmentPagerAdapter mantêm os fragmentos em memória para que
 * eles possam ser utilizados novamente, isso enquanto houver
 * caminho de volta a eles (transição entre Tabs, por exemplo).
 */
class ConfigSectionsAdapter(
    private val context: Context,
    fm: FragmentManager,
    private vararg val fragments: ConfigFormFragment
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        const val TOTAL_PAGES = 2
        const val FIRST_PAGE_POS = 0
        const val SECOND_PAGE_POS = 1
    }

    /*
     * getItem() é invocado para devolver uma instância do
     * fragmento correspondendo a posição (seção/página)
     * informada.
     * */
    override fun getItem(position: Int) = when (position) {
        FIRST_PAGE_POS -> fragments[FIRST_PAGE_POS]
        else -> fragments[SECOND_PAGE_POS]
    }

    override fun getPageTitle(position: Int) = context.getString(
        when (position) {
            FIRST_PAGE_POS -> fragments[FIRST_PAGE_POS].title()
            else -> fragments[SECOND_PAGE_POS].title()
        }
    )

    override fun getCount() = TOTAL_PAGES
}