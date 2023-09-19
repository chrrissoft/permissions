package com.chrrissoft.permissions.location

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat.Builder
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.chrrissoft.permissions.Constants.GENERAL_CHANNEL_ID
import com.chrrissoft.permissions.R
import com.chrrissoft.permissions.Util.isGreatBackgroundLocation
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
        var time = 0
        while (time < 100) {
            notificationManager.notify(NOTIFICATION_ID, createNotification(time))
            delay(10_000)
            time++
        }
        return Result.success()
    }

    private fun createNotification(
        counter: Int,
        isGreat: Boolean = context.isGreatBackgroundLocation,
    ): Notification {
        val text = """
            Background location permission: $isGreat
        """.trimIndent()
        return Builder(context, GENERAL_CHANNEL_ID)
            .setContentTitle("Background Location Worker $counter/100")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentText(text)
            .setContentIntent(workManager.createCancelPendingIntent(id))
            .build()
    }
}
