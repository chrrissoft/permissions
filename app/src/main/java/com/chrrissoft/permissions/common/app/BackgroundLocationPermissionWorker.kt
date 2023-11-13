package com.chrrissoft.permissions.common.app

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat.Builder
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.chrrissoft.permissions.Constants.GENERAL_CHANNEL_ID
import com.chrrissoft.permissions.R.drawable.ic_launcher_foreground
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import javax.inject.Inject
import com.chrrissoft.permissions.Constants.BACKGROUND_LOCATION_PERMISSION_WORKER_NOTIFICATION_ID as NOTIFICATION_ID

@HiltWorker
class BackgroundLocationPermissionWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var workManager: WorkManager

    override suspend fun doWork(): Result {
        notificationManager.notify(NOTIFICATION_ID, createNotification())
        delay((700_000))
        notificationManager.cancel(NOTIFICATION_ID)
        return Result.success()
    }

    private fun createNotification(): Notification {
        return Builder(context, GENERAL_CHANNEL_ID)
            .setContentTitle("Acceding Background Location")
            .setSmallIcon(ic_launcher_foreground)
            .setContentText("Tap to stop")
            .setProgress((100), (0), (true))
            .setContentIntent(workManager.createCancelPendingIntent(id))
            .build()
    }
}
