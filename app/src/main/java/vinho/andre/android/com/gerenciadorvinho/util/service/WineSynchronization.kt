package vinho.andre.android.com.gerenciadorvinho.util.service

import android.app.job.JobParameters
import android.app.job.JobService
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.data.firebase.service.DataBaseSyncData
import vinho.andre.android.com.gerenciadorvinho.data.sqlite.DBHelper
import vinho.andre.android.com.gerenciadorvinho.interfaces.data.service.DataBaseSyncDataInterface
import vinho.andre.android.com.gerenciadorvinho.util.DataUtil
import vinho.andre.android.com.gerenciadorvinho.util.SharedPreferencesUtil
import vinho.andre.android.com.gerenciadorvinho.util.function.createNotificationChannel

class WineSynchronization : JobService() {

    companion object {
        const val JobId = 123
        const val KeyDateSync = "Sync"
        const val WineBottles = "wineBottles"
    }

    private val notificationId = 321
    private var params: JobParameters? = null
    private val data: DataBaseSyncDataInterface =
        DataBaseSyncData(this, this)

    private lateinit var builder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManagerCompat

    override fun onStartJob(
        params: JobParameters?
    ): Boolean {
        this.params = params
        GlobalScope.launch {
            displayNotificationImage()
            data.syncWines()
        }
        return true
    }

    override fun onStopJob(
        params: JobParameters?
    ): Boolean {
        setContentText(R.string.finished_early)
        notifyManager()
        return true
    }

    private fun notifyManager() =
        notificationManager.notify(
            notificationId,
            builder.build()
        )

    fun getParams(): JobParameters? = params

    fun finishJob(
        params: JobParameters?
    ) {
        val sp = SharedPreferencesUtil(this)

        sp.saveShared(
            KeyDateSync,
            DataUtil().getDateOfSync()
        )

        sp.saveShared(
            WineBottles,
            DBHelper(
                this
            ).getSumVintage().toString()
        )

        setContentText(R.string.successfully_ended)
        notifyManager()
        jobFinished(
            params, false
        )
    }

    private fun displayNotificationImage() {
        createNotificationChannel(getString(R.string.channelIdNotification), this)

        notificationManager = NotificationManagerCompat.from(
            this
        )

        builder = NotificationCompat.Builder(this, getString(R.string.channelIdNotification))
            .setSmallIcon(R.drawable.ic_sync)
            .setContentTitle(getString(R.string.synchronizing_database))
            .setContentText(getString(R.string.in_progress))
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        notifyManager()
    }

    private fun setContentText(
        textID: Int
    ) {
        builder.setContentText(
            getString(textID)
        ).setSmallIcon(
            R.drawable.ic_check
        ).setTimeoutAfter(
            2 * 60 * 1000
        )
    }
}