package vinho.andre.android.com.gerenciadorvinho.util

import android.content.Context
import android.content.SharedPreferences
import vinho.andre.android.com.gerenciadorvinho.R

class SharedPreferencesUtil(
    private var context: Context
) {
    private val preference: String =
        "vinho.andre.android.com.gerenciadorvinho.PREF"

    fun getPreference(): SharedPreferences? =
        context.getSharedPreferences(
            preference,
            Context.MODE_PRIVATE
        )

    fun saveShared(
        string: String,
        information: String
    ) {
        context.getSharedPreferences(
            preference,
            Context.MODE_PRIVATE
        ).edit().putString(string, information).apply()
    }

    fun getAShared(
        string: String
    ): String {
        return context.getSharedPreferences(
            preference,
            Context.MODE_PRIVATE
        ).getString(
            string,
            ""
        )!!
    }

    fun saveAppOpenFilter(
        idFilter: Int
    ) {
        context.getSharedPreferences(
            preference,
            Context.MODE_PRIVATE
        ).edit().putInt("idFilter", idFilter).apply()
    }

    fun getAppOpenFilter(): Int {
        return context.getSharedPreferences(
            preference,
            Context.MODE_PRIVATE
        ).getInt(
            "idFilter",
            R.id.nav_name
        )
    }

    fun saveIsNewUser(
        isNewUser: Boolean?
    ) {
        context.getSharedPreferences(
            preference,
            Context.MODE_PRIVATE
        ).edit().putBoolean(
            "isNewUser",
            isNewUser ?: true
        ).apply()
    }

    fun getIsNewUser(): Boolean {
        return context.getSharedPreferences(
            preference,
            Context.MODE_PRIVATE
        ).getBoolean(
            "isNewUser",
            false
        )
    }

    fun resetSharedPreference() {
        context.getSharedPreferences(
            preference,
            Context.MODE_PRIVATE
        ).edit().clear().apply()
    }
}