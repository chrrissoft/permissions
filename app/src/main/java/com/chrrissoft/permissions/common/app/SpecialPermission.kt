package com.chrrissoft.permissions.common.app

import android.os.Build.VERSION.SDK_INT

sealed interface SpecialPermission {
    val enabled: Boolean
    val supportedApi: Int
    val available get() = SDK_INT >= supportedApi

    data class WriteSettings(
        override val supportedApi: Int = 1,
        override val enabled: Boolean = false
    ) : SpecialPermission

    data class ManageMedia(
        override val supportedApi: Int = 31,
        override val enabled: Boolean = false
    ) : SpecialPermission

    data class SystemAlertWindow(
        override val supportedApi: Int = 1,
        override val enabled: Boolean = false
    ) : SpecialPermission

    data class ScheduleExactAlarm(
        override val supportedApi: Int = 31,
        override val enabled: Boolean = false
    ) : SpecialPermission

    data class DoNotDisturbMode(
        override val supportedApi: Int = 0,
        override val enabled: Boolean = false
    ) : SpecialPermission

    data class ManageExternalStorage(
        override val supportedApi: Int = 30,
        override val enabled: Boolean = false
    ) : SpecialPermission

    data class RequestIgnoreBatteryOptimizations(
        override val supportedApi: Int = 23 ,
        override val enabled: Boolean = false
    ) : SpecialPermission

    data class PackageUsageStats(
        override val supportedApi: Int = 23,
        override val enabled: Boolean = false
    ) : SpecialPermission

    companion object {
        val permissions = buildList {
            add(WriteSettings())
            add(ManageMedia())
            add(SystemAlertWindow())
            add(ScheduleExactAlarm())
            add(DoNotDisturbMode())
            add(ManageExternalStorage())
            add(RequestIgnoreBatteryOptimizations())
            add(PackageUsageStats())
        }
    }
}
