package com.chrrissoft.permissions

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Configuration.Builder
import com.chrrissoft.permissions.custom.app.PermissionReceiver.*
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
        ContextCompat.registerReceiver((this), Normal0(), Normal0.filter, ("com.chrrissoft.permissions.permissions.NORMAL_PERMISSION_0"), null, ContextCompat.RECEIVER_EXPORTED)
        ContextCompat.registerReceiver((this), Normal1(), Normal1.filter, ("com.chrrissoft.permissions.permissions.NORMAL_PERMISSION_1"), null, ContextCompat.RECEIVER_EXPORTED)
        ContextCompat.registerReceiver((this), Normal2(), Normal2.filter, ("com.chrrissoft.permissions.permissions.NORMAL_PERMISSION_2"), null, ContextCompat.RECEIVER_EXPORTED)
        ContextCompat.registerReceiver((this), RunTime0(), RunTime0.filter, ("com.chrrissoft.permissions.permissions.RUN_TIME_PERMISSION_0"), null, ContextCompat.RECEIVER_EXPORTED)
        ContextCompat.registerReceiver((this), RunTime1(), RunTime1.filter, ("com.chrrissoft.permissions.permissions.RUN_TIME_PERMISSION_1"), null, ContextCompat.RECEIVER_EXPORTED)
        ContextCompat.registerReceiver((this), RunTime2(), RunTime2.filter, ("com.chrrissoft.permissions.permissions.RUN_TIME_PERMISSION_2"), null, ContextCompat.RECEIVER_EXPORTED)
        ContextCompat.registerReceiver((this), Signature0(), Signature0.filter, ("com.chrrissoft.permissions.permissions.SIGNATURE_PERMISSION_0"), null, ContextCompat.RECEIVER_EXPORTED)
        ContextCompat.registerReceiver((this), Signature1(), Signature1.filter, ("com.chrrissoft.permissions.permissions.SIGNATURE_PERMISSION_1"), null, ContextCompat.RECEIVER_EXPORTED)
        ContextCompat.registerReceiver((this), Signature2(), Signature2.filter, ("com.chrrissoft.permissions.permissions.SIGNATURE_PERMISSION_2"), null, ContextCompat.RECEIVER_EXPORTED)
        createChannels()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    private fun createChannels() {
        if (SDK_INT < O) return
        notificationManager.createNotificationChannel(generalNotificationChannel)
    }
}
