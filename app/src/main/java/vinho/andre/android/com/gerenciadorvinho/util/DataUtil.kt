package vinho.andre.android.com.gerenciadorvinho.util

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object DataUtil {
    private var isValid: Boolean = true
    private var patter: String = "dd/MM/yyyy"

    fun getIsValid(): Boolean = isValid

    fun getCurrentDateTime(): Date {
        return Calendar
            .getInstance()
            .time
    }

    fun dateToString(
        data: Date,
        locale: Locale = Locale.getDefault()
    ): String {
        return SimpleDateFormat(
            patter,
            locale
        ).format(
            data
        )
    }

    fun isDate(
        string_date: String
    ): Date {
        return try {
            val simpleDateFormat = SimpleDateFormat(
                patter,
                Locale.getDefault()
            )

            simpleDateFormat.isLenient = false

            simpleDateFormat.parse(
                string_date
            )
        } catch (e: Exception) {
            Log.e(
                "DataUtil",
                "Error formatting date"
            )
            isValid = false
            return Date()
        }
    }

    fun generateNameForImage(): String {
        patter = "ddMMyyyy_HHmmss"
        return "MV_${dateToString(getCurrentDateTime())}.jpg"
    }

    fun getDateOfSync(): String {
        patter = "dd/MM/yyyy HH:mm:ss"
        return dateToString(getCurrentDateTime())
    }
}