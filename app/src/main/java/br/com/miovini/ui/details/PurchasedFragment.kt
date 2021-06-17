package br.com.miovini.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.miovini.databinding.FragmentPurchasedBinding
import br.com.miovini.models.Purhased
import br.com.miovini.ui.details.adapter.PurchasedAdapter

class PurchasedFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentPurchasedBinding? = null

    private val adapter by lazy { PurchasedAdapter() }
//    private val toolbar by lazy { (requireActivity() as MainActivity).supportActionBar }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPurchasedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        toolbar?.subtitle = "Vinho 1"
        adapter.submitList(
            listOf(
                Purhased(
                    id = "1",
                    amount = 10,
                    price = 1000f,
                    vintage = "2014",
                    note = "Ok",
                    storeName = "Evino",
                    purchasedDate = 100L
                ),
                Purhased(
                    id = "2",
                    amount = 100,
                    price = 3000f,
                    vintage = "2020",
                    note = "Ok OK",
                    storeName = "Evino",
                    purchasedDate = 100L
                )
            )
        )

        binding.rvVintage.setHasFixedSize(true)
        binding.rvVintage.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}