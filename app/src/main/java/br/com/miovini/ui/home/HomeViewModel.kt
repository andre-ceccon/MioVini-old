package br.com.miovini.ui.home

import android.app.Activity
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ConcatAdapter
import br.com.miovini.R
import br.com.miovini.models.generateSorte
import br.com.miovini.models.genetareWineList
import br.com.miovini.ui.home.adapter.SortAdapter
import br.com.miovini.ui.home.adapter.WineAdapter
import kotlinx.coroutines.*

class HomeViewModel : ViewModel() {

    companion object {
        private const val viewfliper_wines = 1
        private const val viewfliper_loading = 0
    }

    val viewFlipperLiveData: MutableLiveData<Int> = MutableLiveData()

    val sortAdapter: SortAdapter by lazy { SortAdapter() }
    val wineAdapter: WineAdapter by lazy { WineAdapter() }
    val adapter = ConcatAdapter(sortAdapter, wineAdapter)

    private var actionMode: ActionMode? = null
    private val actionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(
            mode: ActionMode, menu: Menu
        ): Boolean {
            mode.menuInflater.inflate(
                R.menu.actionmode_edit_delete_menu, menu
            )
            return true
        }

        override fun onPrepareActionMode(
            mode: ActionMode, menu: Menu
        ): Boolean = false

        override fun onActionItemClicked(
            mode: ActionMode, item: MenuItem
        ): Boolean {
            return when (item.itemId) {
                else -> false
            }
        }

        override fun onDestroyActionMode(
            mode: ActionMode
        ) {
            wineAdapter.selectedItems.clear()
            wineAdapter.currentList.filter { it.selectedItem }.forEach { it.selectedItem = false }
            wineAdapter.notifyDataSetChanged()
            actionMode = null
        }
    }

    private suspend fun loadUserData() {
        return coroutineScope {
            withContext(Dispatchers.Default) {
                sortAdapter.submitList(generateSorte())
                wineAdapter.submitList(genetareWineList())

                withContext(Dispatchers.Main) {
                    viewFlipperLiveData.value = viewfliper_wines
                }
            }
        }
    }

    fun getWines() {
        GlobalScope.launch(context = Dispatchers.Main) {
            viewFlipperLiveData.value = viewfliper_loading
            loadUserData()
        }
    }

    fun enableActionMode(
        activity: Activity,
        position: Int
    ) {
        if (actionMode == null) {
            actionMode = activity.startActionMode(actionModeCallback)
        }

        wineAdapter.toggleSelection(position = position)
        val sizeListAdapter = wineAdapter.selectedItems.size()
        if (sizeListAdapter == 0) {
            actionMode?.finish()
        } else {
            actionMode?.title = "$sizeListAdapter"
            actionMode?.invalidate()
        }
    }
}