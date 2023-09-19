package com.chrrissoft.permissions.location

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.N
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.chrrissoft.permissions.Constants
import com.chrrissoft.permissions.R
import com.chrrissoft.permissions.Util.isGreatCoarseLocation
import com.chrrissoft.permissions.Util.isGreatFineLocation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject
import com.chrrissoft.permissions.Constants.FOREGROUND_LOCATION_PERMISSION_SERVICE_NOTIFICATION_ID as NOTIFICATION_ID

@AndroidEntryPoint
class ForegroundLocationPermissionService : Service() {
    @Inject
    lateinit var notificationManager: NotificationManager

    private val scope = CoroutineScope(Job())
    var wasStarted = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action==Constants.STOP_FOREGROUND_ACTION) {
            if (SDK_INT >= N) {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()

            } else {
                stopForeground(true)
                stopSelf()
            }
            scope.cancel()
            debug("stopping service")
            return START_NOT_STICKY
        }
        debug("starting service")
        if (wasStarted) return START_NOT_STICKY

        var counter = 0
        startForeground(NOTIFICATION_ID, createNotification(counter = counter))
        scope.launch {
            while (counter < 100) {
                delay(timeMillis = 8000)
                counter++
                notificationManager.notify(NOTIFICATION_ID, createNotification(counter = counter))
            }
        }

        wasStarted = true
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotification(
        counter: Int,
        coarseLocation: Boolean = isGreatCoarseLocation,
        fineLocation: Boolean = isGreatFineLocation,
    ): Notification {
        val text = "Coarse location: $coarseLocation\nFine location: $fineLocation"
        return NotificationCompat.Builder(this, Constants.GENERAL_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Foreground Location Service: $counter/100")
            .setContentText(text)
            .setContentIntent(createContentIntent())
            .build()
    }

    private fun createContentIntent(): PendingIntent {
        val intent = Intent((this), ForegroundLocationPermissionService::class.java).apply {
            action = Constants.STOP_FOREGROUND_ACTION
        }
        return PendingIntent.getService((this), (0), intent, PendingIntent.FLAG_IMMUTABLE)
    }

    private companion object {
        private fun debug(message: Any?) {
            Log.d(TAG, "$message")
        }

        private const val TAG = "ForegroundLocationPermissionService"
    }

}
