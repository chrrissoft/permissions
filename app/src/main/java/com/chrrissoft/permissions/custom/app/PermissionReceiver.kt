package com.chrrissoft.permissions.custom.app

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.chrrissoft.permissions.Util.generalNotification
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.random.Random.Default.nextInt

abstract class PermissionReceiver(private val title: String, private val action: String) :
    BroadcastReceiver() {
    @Inject
    lateinit var notificationManager: NotificationManager
    override fun onReceive(context: Context, intent: Intent) {
        if (action != intent.action) return
        generalNotification(context, title, subText = "Receiver").build().let {
            notificationManager.notify(nextInt(), it)
        }
    }

    @AndroidEntryPoint
    class Normal0 @Inject constructor() :
        PermissionReceiver(title = "Normal 0", ACTION_NORMAL_0) {
        companion object {
            val filter = IntentFilter(ACTION_NORMAL_0)
        }
    }

    @AndroidEntryPoint
    class Normal1 @Inject constructor() :
        PermissionReceiver(title = "Normal 1", ACTION_NORMAL_1) {
        companion object {
            val filter = IntentFilter(ACTION_NORMAL_1)
        }
    }

    @AndroidEntryPoint
    class Normal2 @Inject constructor() :
        PermissionReceiver(title = "Normal 2", ACTION_NORMAL_2) {
        companion object {
            val filter = IntentFilter(ACTION_NORMAL_2)
        }
    }


    @AndroidEntryPoint
    class RunTime0 @Inject constructor() :
        PermissionReceiver(title = "Run time 0", ACTION_RUN_TIME_0) {
        companion object {
            val filter = IntentFilter(ACTION_RUN_TIME_0)
        }
    }

    @AndroidEntryPoint
    class RunTime1 @Inject constructor() :
        PermissionReceiver(title = "Run time 1", ACTION_RUN_TIME_1) {
        companion object {
            val filter = IntentFilter(ACTION_RUN_TIME_1)
        }
    }

    @AndroidEntryPoint
    class RunTime2 @Inject constructor() :
        PermissionReceiver(title = "Run time 2", ACTION_RUN_TIME_2) {
        companion object {
            val filter = IntentFilter(ACTION_RUN_TIME_2)
        }
    }

    @AndroidEntryPoint
    class Signature0 @Inject constructor() :
        PermissionReceiver(title = "Signature 0", ACTION_SIGNATURE_0) {
        companion object {
            val filter = IntentFilter(ACTION_SIGNATURE_0)
        }
    }

    @AndroidEntryPoint
    class Signature1 @Inject constructor() :
        PermissionReceiver(title = "Signature 1", ACTION_SIGNATURE_1) {
        companion object {
            val filter = IntentFilter(ACTION_SIGNATURE_1)
        }
    }

    @AndroidEntryPoint
    class Signature2 @Inject constructor() :
        PermissionReceiver(title = "Signature 2", ACTION_SIGNATURE_2) {
        companion object {
            val filter = IntentFilter(ACTION_SIGNATURE_2)
        }
    }

    private companion object {
        private const val ACTION_NORMAL_0 = "com.chrrissoft.permissions.ACTION_NORMAL_0"
        private const val ACTION_NORMAL_1 = "com.chrrissoft.permissions.ACTION_NORMAL_1"
        private const val ACTION_NORMAL_2 = "com.chrrissoft.permissions.ACTION_NORMAL_2"
        private const val ACTION_RUN_TIME_0 = "com.chrrissoft.permissions.ACTION_RUN_TIME_0"
        private const val ACTION_RUN_TIME_1 = "com.chrrissoft.permissions.ACTION_RUN_TIME_1"
        private const val ACTION_RUN_TIME_2 = "com.chrrissoft.permissions.ACTION_RUN_TIME_2"
        private const val ACTION_SIGNATURE_0 = "com.chrrissoft.permissions.ACTION_SIGNATURE_0"
        private const val ACTION_SIGNATURE_1 = "com.chrrissoft.permissions.ACTION_SIGNATURE_1"
        private const val ACTION_SIGNATURE_2 = "com.chrrissoft.permissions.ACTION_SIGNATURE_2"
    }
}
