package vinho.andre.android.com.gerenciadorvinho.interfaces.presenter

import com.google.android.gms.tasks.Task
import vinho.andre.android.com.gerenciadorvinho.domain.Wine

interface WineRegisterPresenterInterface {

    fun onSaveWine(
        hasImage: Boolean,
        isUpdateOf: String?,
        hasComment: Boolean,
        hasPurchase: Boolean
    )

    fun onSaveImage()

    fun notifyProgressNotification(
        progress: Int
    )

    fun notifyUploadResult(
        result: Boolean
    )

    fun onWineRescueResponse(
        task: Task<Void>?,
        wine: Wine
    )
}