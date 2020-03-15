package vinho.andre.android.com.gerenciadorvinho.util.function

import android.app.job.JobScheduler
import android.content.Context
import vinho.andre.android.com.gerenciadorvinho.util.service.WineSynchronization

fun isJobServiceOn(
    context: Context
): Boolean {
    val scheduler = context.getSystemService(
        Context.JOB_SCHEDULER_SERVICE
    ) as JobScheduler

    var hasBeenScheduled = false

    for (jobInfo in scheduler.allPendingJobs) {
        if (jobInfo.id == WineSynchronization.JobId) {
            hasBeenScheduled = true
            break
        }
    }

    return hasBeenScheduled
}