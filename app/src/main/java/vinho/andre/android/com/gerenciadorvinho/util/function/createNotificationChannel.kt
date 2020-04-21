package vinho.andre.android.com.gerenciadorvinho.util.function

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import vinho.andre.android.com.gerenciadorvinho.R

fun createNotificationChannel(
    menssage: String,
    context: Context
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel =
            NotificationChannel(
                menssage,
                context.getString(
                    R.string.app_name
                ),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = menssage
            }

        val notificationManager: NotificationManager =
            context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        notificationManager.createNotificationChannel(
            channel
        )
    }
}