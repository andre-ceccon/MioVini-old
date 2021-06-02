package br.com.miovini.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import br.com.miovini.R
import br.com.miovini.databinding.BottomSheetSortBinding
import br.com.miovini.databinding.HomeFragmentBinding
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
        return binding.root
    }

    override fun onViewCreated(
        view: View, savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        with(homeViewModel) {
            sortAdapter.sortItemClickListener = { createDialog() }
            wineAdapter.wineItemClickListener = {
                findNavController().navigate(R.id.action_navigation_home_to_detailsFragment)
            }

            binding.recyclerWine.adapter = ConcatAdapter(sortAdapter, wineAdapter)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createDialog() {
        val bining = BottomSheetSortBinding.inflate(layoutInflater)
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