package vinho.andre.android.com.gerenciadorvinho.util

import java.text.SimpleDateFormat
import java.util.*

object DataUtil {
    private var isValid: Boolean = true
    private val patters = mapOf(
        "onlyDate" to "dd/MM/yyyy",
        "dateAndTimeTogether" to "ddMMyyyy_HHmmss",
        "separateDateAndTime" to "dd/MM/yyyy HH:mm:ss"
    )

    fun getIsValid(): Boolean = isValid

    fun getCurrentDateTime(): Date {
        return Calendar
            .getInstance()
            .time
    }

    fun dateToString(
        data: Date,
        patter: String? = patters["onlyDate"]
    ): String {
        return SimpleDateFormat(
            patter,
            Locale.getDefault()
        ).format(
            data
        )
    }

    fun isDate(
        string_date: String
    ): Date {
        return try {
            val simpleDateFormat = SimpleDateFormat(
                patters["onlyDate"],
                Locale.getDefault()
            )

            simpleDateFormat.isLenient = false

            simpleDateFormat.parse(
                string_date
            )
        } catch (e: Exception) {
            isValid = false
            return Date()
        }
    }

    fun generateNameForImage(): String =
        "MV_${dateToString(
            getCurrentDateTime(),
            patters["dateAndTimeTogether"]
        )}.jpg"

    fun getDateOfSync(): String =
        dateToString(
            getCurrentDateTime(),
            patters["separateDateAndTime"]
        )
}