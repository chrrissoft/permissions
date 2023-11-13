package com.chrrissoft.permissions.custom.app

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
import android.os.Build.VERSION.SDK_INT
import android.os.IBinder
import androidx.core.app.ServiceCompat.startForeground
import com.chrrissoft.permissions.Util.generalNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random.Default.nextInt

abstract class PermissionService(private val title: String) : Service() {
    private val scope = CoroutineScope(SupervisorJob())

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val notification =
            generalNotification(ctx = this, title = title, subText = "Service").build()
        val type = if (SDK_INT >= 34) FOREGROUND_SERVICE_TYPE_SPECIAL_USE else 0
        startForeground((this), (nextInt()), notification, type)
        scope.launch { delay(timeMillis = 5000).apply { stopSelf() } }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        scope.cancel()
    }

    @AndroidEntryPoint
    class Normal0 @Inject constructor() : PermissionService(title = "Normal 0")

    @AndroidEntryPoint
    class Normal1 @Inject constructor() : PermissionService(title = "Normal 1")

    @AndroidEntryPoint
    class Normal2 @Inject constructor() : PermissionService(title = "Normal 2")


    @AndroidEntryPoint
    class RunTime0 @Inject constructor() : PermissionService(title = "Run time 0")

    @AndroidEntryPoint
    class RunTime1 @Inject constructor() : PermissionService(title = "Run time 1")

    @AndroidEntryPoint
    class RunTime2 @Inject constructor() : PermissionService(title = "Run time 2")

    @AndroidEntryPoint
    class Signature0 @Inject constructor() : PermissionService(title = "Signature 0")

    @AndroidEntryPoint
    class Signature1 @Inject constructor() : PermissionService(title = "Signature 1")

    @AndroidEntryPoint
    class Signature2 @Inject constructor() : PermissionService(title = "Signature 2")
}
