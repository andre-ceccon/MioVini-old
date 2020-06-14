package vinho.andre.android.com.gerenciadorvinho.presenter

import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.tasks.Task
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.data.firebase.WineRegisterData
import vinho.andre.android.com.gerenciadorvinho.domain.Wine
import vinho.andre.android.com.gerenciadorvinho.interfaces.data.WineRegisterDataInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.WineRegisterPresenterInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.view.WineRegisterViewInterface
import vinho.andre.android.com.gerenciadorvinho.util.function.createNotificationChannel

class WineRegisterPresenter(
    val view: WineRegisterViewInterface
) : WineRegisterPresenterInterface {

    private val notificationId = 2
    private lateinit var database: WineRegisterDataInterface
    private lateinit var builder: NotificationCompat.Builder

    override fun onSaveWine(
        hasImage: Boolean,
        isUpdateOf: String?,
        hasComment: Boolean,
        hasPurchase: Boolean
    ) {
        database = WineRegisterData(
            view.getContext(),
            view.getImage(),
            isUpdateOf,
            this
        )

        when {
            hasPurchase && hasComment -> {
                database.onSaveWine(
                    view.getWine(),
                    view.getComment(),
                    view.getPurchase(),
                    view.getWineComplement()
                )
            }
            hasPurchase -> {
                database.onSaveWine(
                    view.getWine(),
                    view.getPurchase(),
                    view.getWineComplement()
                )
            }
            hasComment -> {
                database.onSaveWine(
                    view.getWine(),
                    view.getComment(),
                    view.getWineComplement()
                )
            }
            else -> {
                database.onSaveWine(
                    view.getWine(),
                    view.getWineComplement()
                )
            }
        }
    }

    override fun onSaveImage() {
        displayNotificationImage()
        database.onUploadImage()
    }

    override fun notifyUploadResult(
        result: Boolean
    ) {
        builder.setContentText(
            if (result) {
                view.getContext().getString(R.string.success_in_operation)
            } else {
                view.getContext().getString(R.string.error_upload_image)
            }
        ).setProgress(
            0,
            0,
            false
        ).setTimeoutAfter(
            1 * 60 * 1000
        )

        notificationManagerNotify()
    }

    override fun onWineRescueResponse(
        task: Task<Void>?,
        wine: Wine
    ) {
        if (task != null && task.isSuccessful) {
            view.callWineDetailsActivity(
                wine
            )
        } else {
            Toast.makeText(
                view.getContext(),
                view.getContext().getString(R.string.error_saving),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun notifyProgressNotification(
        progress: Int
    ) {
        builder.setProgress(
            100,
            progress,
            false
        )

        notificationManagerNotify()
    }

    private fun notificationManagerNotify() =
        NotificationManagerCompat.from(
            view.getContext()
        ).notify(
            notificationId,
            builder.build()
        )

    private fun displayNotificationImage() {
        createNotificationChannel(
            view.getContext().getString(R.string.channelIdNotification),
            view.getContext()
        )

        builder = NotificationCompat.Builder(
            view.getContext(),
            view.getContext().getString(R.string.channelIdNotification)
        ).setSmallIcon(R.drawable.ic_file_upload)
            .setContentTitle(view.getContext().getString(R.string.title_notification_upload_image))
            .setContentText(view.getContext().getString(R.string.upload_in_progress))
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setProgress(100, 0, false)

        notificationManagerNotify()
    }
}