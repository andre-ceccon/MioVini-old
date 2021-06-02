package br.com.miovini.ui.home

import androidx.lifecycle.ViewModel
import br.com.miovini.models.generateSorte
import br.com.miovini.models.genetareWineList
import br.com.miovini.ui.home.adapter.SortAdapter
import br.com.miovini.ui.home.adapter.WineAdapter

class HomeViewModel : ViewModel() {

    val sortAdapter: SortAdapter by lazy {
        SortAdapter().apply { submitList(generateSorte()) }
    }

    val wineAdapter: WineAdapter by lazy {
        WineAdapter().apply { submitList(genetareWineList()) }
    }
}