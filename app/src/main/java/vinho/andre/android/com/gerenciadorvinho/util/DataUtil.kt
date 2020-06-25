package vinho.andre.android.com.gerenciadorvinho.util

import java.text.SimpleDateFormat
import java.util.*

object DataUtil {
    private var isValid: Boolean = true

    private fun englishPatter(): String = "MM/dd/yyyy"

    private fun portuguesePatter(): String = "dd/MM/yyyy"

    private fun isEnglishLanguage(): Boolean =
        Locale.getDefault().displayLanguage == "English"

    private fun patter(
        type: String
    ): String {
        return when (type) {
            "onlyDate" -> {
                if (isEnglishLanguage()) englishPatter() else portuguesePatter()
            }
            "dateAndTimeTogether" -> {
                if (isEnglishLanguage()) "${englishPatter()}_HHmmss" else "${portuguesePatter()}_HHmmss"
            }
            "separateDateAndTime" -> {
                if (isEnglishLanguage()) "${englishPatter()} HH:mm:ss" else "${portuguesePatter()} HH:mm:ss"
            }
            else -> {
                ""
            }
        }
    }

    fun getIsValid(): Boolean = isValid

    fun getCurrentDateTime(): Date {
        return Calendar
            .getInstance()
            .time
    }

    fun dateToString(
        data: Date,
        patter: String? = patter("onlyDate")
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
                patter("onlyDate"),
                Locale.getDefault()
            )

            simpleDateFormat.isLenient = false

            simpleDateFormat.parse(
                string_date
            ) ?: Date()
        } catch (e: Exception) {
            isValid = false
            return Date()
        }
    }

    fun generateNameForImage(): String =
        "MV_${dateToString(
            getCurrentDateTime(),
            patter("dateAndTimeTogether")
        )}.jpg"

    fun getDateOfSync(): String =
        dateToString(
            getCurrentDateTime(),
            patter("separateDateAndTime")
        )
}