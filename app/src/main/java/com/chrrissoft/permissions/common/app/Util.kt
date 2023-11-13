package com.chrrissoft.permissions.common.app

import com.chrrissoft.permissions.common.app.SpecialPermission.*

object Util {
    val SpecialPermission.label get() = run {
        when (this) {
            is DoNotDisturbMode -> "Do not disturb mode"
            is ManageExternalStorage -> "Manage external storage"
            is ManageMedia -> "Manage media"
            is RequestIgnoreBatteryOptimizations -> "Request ignore battery optimizations"
            is ScheduleExactAlarm -> "Schedule exact alarm"
            is SystemAlertWindow -> "System alert window"
            is WriteSettings -> "Write settings"
            is PackageUsageStats -> "Package usage stats"
        }
    }
}
