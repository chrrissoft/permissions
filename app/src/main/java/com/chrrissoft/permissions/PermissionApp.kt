package com.chrrissoft.permissions

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Configuration.Builder
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class PermissionApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var generalNotificationChannel: NotificationChannel

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            debug("creating channel")
            notificationManager.createNotificationChannel(generalNotificationChannel)
        }
    }

    override fun getWorkManagerConfiguration(): Configuration {
        debug("creating work manager configuration")
        return Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    private companion object {
        private fun debug(message: Any?) {
            Log.d(TAG, "$message")
        }

        private const val TAG = "PermissionApp"
    }
}

