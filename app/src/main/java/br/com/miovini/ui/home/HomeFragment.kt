package br.com.miovini.ui.home

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.com.miovini.R
import br.com.miovini.databinding.DialogSortHomeBinding
import br.com.miovini.databinding.HomeFragmentBinding
import br.com.miovini.utils.NavigationExtension.navigateWithAnimations
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeFragment : Fragment() {

    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private var _binding: HomeFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        homeViewModel.getWines()
        return binding.root
    }

    override fun onViewCreated(
        view: View, savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        homeViewModel.viewFlipperLiveData.observe(
            viewLifecycleOwner, { viewFliver ->
                binding.viewFlipperWines.displayedChild = viewFliver
            }
        )
    }

    private fun initRecyclerView() {
        with(homeViewModel) {
            binding.recyclerWine.setHasFixedSize(true)
            binding.recyclerWine.adapter = homeViewModel.adapter

            sortAdapter.sortItemClickListener = { createDialog() }

            wineAdapter.wineItemClickListener = {
                findNavController().navigateWithAnimations(R.id.action_navigation_home_to_detailsFragment)
            }

            wineAdapter.wineItemLongClickListener = { position ->
                homeViewModel.enableActionMode(activity = requireActivity(), position = position)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView(); _binding = null
    }

    private fun createDialog() {
        val bining = DialogSortHomeBinding.inflate(layoutInflater)
        val sortedByArray = resources.getStringArray(R.array.sorted_by_array).apply {
            sortBy { it.replace("\\s", "").length }
        }

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.row_dropdown_sort, sortedByArray)

        MaterialAlertDialogBuilder(
            requireContext()
        ).setView(
            bining.root
        ).create().show()

        with(bining) {
            radioButton.isChecked = true
            autoCompleteTextView.apply {
                setText(sortedByArray[0])
                setAdapter(arrayAdapter)
            }
        }
    }
}

fun Context.isDarkTheme(): Boolean {
    return this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}