package br.com.miovini.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.miovini.databinding.FragmentPurchasedRegistrationBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class PurchasedRegistrationFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentPurchasedRegistrationBinding? = null

    private val datePicker by lazy {
        createDataPicker()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPurchasedRegistrationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tetDatePicker.setOnClickListener {
            datePicker.show(parentFragmentManager, "datePicker")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun createDataPicker(): MaterialDatePicker<Long> {
        val datePicker: MaterialDatePicker<Long> =
            MaterialDatePicker.Builder.datePicker().apply {
                setTitleText(
                    "Select date purchase"
                )
                setCalendarConstraints(
                    CalendarConstraints.Builder().setValidator(
                        DateValidatorPointBackward.now()
                    ).build()
                )
                setSelection(
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            }.build()

        datePicker.addOnPositiveButtonClickListener {
            val offsetFromUTC = TimeZone.getDefault().getOffset(Date().time) * -1
            val simpleFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val date = Date(it + offsetFromUTC)

            binding.tetDatePicker.setText(simpleFormat.format(date))
        }

        return datePicker
    }
}