package vinho.andre.android.com.gerenciadorvinho.util.masks

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.lang.ref.WeakReference
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class MoneyTextWatcher(
    editText: EditText
) : TextWatcher {

    private val locale: Locale
    private val editTextWeakReference: WeakReference<EditText>?
    private val symbolCurrency: String
        get() = NumberFormat.getCurrencyInstance(locale).currency!!.symbol

    init {
        this.editTextWeakReference = WeakReference(editText)
        this.locale = Locale.getDefault()
    }

    override fun beforeTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
    ) {
    }

    override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
    }

    override fun afterTextChanged(editable: Editable) {
        val editText = editTextWeakReference!!.get() ?: return

        editText.removeTextChangedListener(this)

        val parsed = parseToBigDecimal(
            editable.toString().trim {
                it <= ' '
            }
        )

        val formatted = NumberFormat
            .getCurrencyInstance(
                locale
            )
            .format(
                parsed
            ).replace(
                ",",
                "."
            )
            .replace(
                symbolCurrency,
                ""
            )

        editText.setText(formatted)
        editText.setSelection(formatted.length)
        editText.addTextChangedListener(this)
    }

    private fun parseToBigDecimal(
        value: String
    ): BigDecimal {
        val replaceable = String.format(
            "[%s,.\\s]",
            symbolCurrency
        )

        val cleanString = value.replace(
            replaceable.toRegex(),
            ""
        )

        return BigDecimal(cleanString)
            .setScale(
                2,
                BigDecimal.ROUND_FLOOR
            ).divide(
                BigDecimal(
                    100
                ),
                BigDecimal.ROUND_FLOOR
            )
    }
}