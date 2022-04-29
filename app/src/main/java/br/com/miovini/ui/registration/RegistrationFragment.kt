package br.com.miovini.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.miovini.databinding.RegistrationFragmentBinding
import br.com.miovini.utils.NavigationExtension.navigateWithAnimations
import com.blankj.utilcode.util.ToastUtils

class RegistrationFragment : Fragment() {

    private val binding get() = _binding!!
    private lateinit var viewModel: RegistrationViewModel
    private var _binding: RegistrationFragmentBinding? = null

    private val args by lazy { RegistrationFragmentArgs.fromBundle(requireArguments()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegistrationFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(
        view: View, savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnAddPhoto.setOnClickListener {
                sivWine.visibility = if (sivWine.isVisible)
                    View.GONE
                else {
                    ToastUtils.showShort("Processo de tirar a foto")
                    View.VISIBLE
                }
            }

            btnPurchased.setOnClickListener {
                val action = RegistrationFragmentDirections.actionRegistrationFragmentToPurchasedRegistrationFragment()
                findNavController().navigateWithAnimations(action)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}