package com.chrrissoft.permissions.common.app

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
import android.os.Build.VERSION.SDK_INT
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat.startForeground
import com.chrrissoft.permissions.Constants.GENERAL_CHANNEL_ID
import com.chrrissoft.permissions.R.drawable.ic_launcher_foreground
import com.chrrissoft.permissions.StopServiceService
import com.chrrissoft.permissions.StopServiceService.Companion.EXTRA_SERVICE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject
import com.chrrissoft.permissions.Constants.FOREGROUND_LOCATION_PERMISSION_SERVICE_NOTIFICATION_ID as NOTIFICATION_ID

@AndroidEntryPoint
class ForegroundLocationPermissionService : Service() {
    @Inject
    lateinit var notificationManager: NotificationManager

    private val scope = CoroutineScope(Job())
    private var started = false

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (started) return START_NOT_STICKY
        val type = if (SDK_INT >= 34) FOREGROUND_SERVICE_TYPE_SPECIAL_USE else 0
        startForeground((this), NOTIFICATION_ID, createNotification(), type)
        scope.launch {
            delay(timeMillis = 700_000)
            notificationManager.cancel(NOTIFICATION_ID)
        }

        started = true
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(): Notification {
        return NotificationCompat.Builder((this), GENERAL_CHANNEL_ID)
            .setSmallIcon(ic_launcher_foreground)
            .setContentTitle("Acceding Foreground Location")
            .setContentText("Tap to stop")
            .setProgress((100), (0), (true))
            .setContentIntent(createContentIntent())
            .build()
    }

    private fun createContentIntent(): PendingIntent {
        val componentName by lazy { ComponentName((this), this::class.java) }
        val intent = Intent((this), StopServiceService::class.java)
            .putExtra(EXTRA_SERVICE, componentName)
        return PendingIntent.getService((this), (0), intent, FLAG_IMMUTABLE)
    }
}
