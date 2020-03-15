package vinho.andre.android.com.gerenciadorvinho.domain

import androidx.appcompat.app.AppCompatActivity

data class SettingItem(
    val label: String,
    val description: String,
    val activityClass: Class<out AppCompatActivity>
)