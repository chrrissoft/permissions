package com.chrrissoft.permissions.common.usecases.classes

import android.Manifest.permission.MANAGE_MEDIA
import android.Manifest.permission.PACKAGE_USAGE_STATS
import android.app.AlarmManager
import android.app.NotificationManager
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.R
import android.os.Build.VERSION_CODES.S
import android.os.Environment.isExternalStorageManager
import android.os.PowerManager
import android.provider.Settings.System.canWrite
import android.provider.Settings.canDrawOverlays
import com.chrrissoft.permissions.PermissionApp
import com.chrrissoft.permissions.Util.canScheduleExactAlarmsCompat
import com.chrrissoft.permissions.Util.hasPermission
import com.chrrissoft.permissions.common.app.SpecialPermission
import com.chrrissoft.permissions.common.app.SpecialPermission.DoNotDisturbMode
import com.chrrissoft.permissions.common.app.SpecialPermission.ManageExternalStorage
import com.chrrissoft.permissions.common.app.SpecialPermission.ManageMedia
import com.chrrissoft.permissions.common.app.SpecialPermission.PackageUsageStats
import com.chrrissoft.permissions.common.app.SpecialPermission.RequestIgnoreBatteryOptimizations
import com.chrrissoft.permissions.common.app.SpecialPermission.ScheduleExactAlarm
import com.chrrissoft.permissions.common.app.SpecialPermission.SystemAlertWindow
import com.chrrissoft.permissions.common.app.SpecialPermission.WriteSettings
import com.chrrissoft.permissions.common.usecases.interfaces.ResolveSpecialPermissionEnabledUseCase
import javax.inject.Inject

class ResolveSpecialPermissionEnabledUseCaseImpl @Inject constructor(
    private val app: PermissionApp,
    private val alarmManager: AlarmManager,
    private val powerManager: PowerManager,
    private val notificationManager: NotificationManager,
) : ResolveSpecialPermissionEnabledUseCase {
    override fun invoke(data: SpecialPermission): SpecialPermission {
        return when (data) {
            is WriteSettings -> data.copy(enabled = canWrite(app))
            is ManageMedia ->  data.copy(enabled = if (SDK_INT >= S) app.hasPermission(MANAGE_MEDIA) else false)
            is SystemAlertWindow -> data.copy(enabled = canDrawOverlays(app))
            is ScheduleExactAlarm -> data.copy(enabled = alarmManager.canScheduleExactAlarmsCompat())
            is DoNotDisturbMode -> data.copy(enabled = notificationManager.isNotificationPolicyAccessGranted)
            is ManageExternalStorage ->
                data.copy(enabled = if (SDK_INT >= R) isExternalStorageManager() else false)
            is RequestIgnoreBatteryOptimizations ->
                data.copy(enabled = powerManager.isIgnoringBatteryOptimizations(app.packageName))
            is PackageUsageStats -> data.copy(enabled = app.hasPermission(PACKAGE_USAGE_STATS))
        }
    }
}
