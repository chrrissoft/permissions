package com.chrrissoft.permissions.ui.components

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri.parse
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.R
import android.os.Build.VERSION_CODES.S
import android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
import android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION
import android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS
import android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS
import android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
import android.provider.Settings.ACTION_REQUEST_MANAGE_MEDIA
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.automirrored.rounded.Launch
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults.iconButtonColors
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.chrrissoft.permissions.common.app.SpecialPermission
import com.chrrissoft.permissions.common.app.SpecialPermission.DoNotDisturbMode
import com.chrrissoft.permissions.common.app.SpecialPermission.ManageExternalStorage
import com.chrrissoft.permissions.common.app.SpecialPermission.ManageMedia
import com.chrrissoft.permissions.common.app.SpecialPermission.PackageUsageStats
import com.chrrissoft.permissions.common.app.SpecialPermission.RequestIgnoreBatteryOptimizations
import com.chrrissoft.permissions.common.app.SpecialPermission.ScheduleExactAlarm
import com.chrrissoft.permissions.common.app.SpecialPermission.SystemAlertWindow
import com.chrrissoft.permissions.common.app.SpecialPermission.WriteSettings
import com.chrrissoft.permissions.common.app.Util.label

@SuppressLint("BatteryLife")
@Composable
fun SpecialPermission(
    state: SpecialPermission,
    modifier: Modifier = Modifier,
) {
    MyTextField(
        enabled = false,
        onValueChange = {},
        value = state.label,
        trailingIcon = {
            val ctx = LocalContext.current
            IconButton(
                onClick = {
                    val action = when (state) {
                        is DoNotDisturbMode -> ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS
                        is ManageExternalStorage -> if (SDK_INT >= R) ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION else ""
                        is ManageMedia -> if (SDK_INT >= S) ACTION_REQUEST_MANAGE_MEDIA else ""
                        is RequestIgnoreBatteryOptimizations -> ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                        is ScheduleExactAlarm -> if (SDK_INT >= S) ACTION_REQUEST_SCHEDULE_EXACT_ALARM else ""
                        is SystemAlertWindow -> ACTION_MANAGE_OVERLAY_PERMISSION
                        is WriteSettings -> ACTION_MANAGE_WRITE_SETTINGS
                        is PackageUsageStats -> ACTION_USAGE_ACCESS_SETTINGS
                    }
                    val intent = Intent(action)
                        .apply { data = parse(("package:com.chrrissoft.permissions")) }
                    try {
                        ctx.startActivity(intent)
                    } catch (_: Throwable) {

                    }
                },
                enabled = state.available,
                content = { Icon(Icons.AutoMirrored.Rounded.Launch, (null)) },
                colors = iconButtonColors(contentColor = colorScheme.onPrimaryContainer),
            )
        },
        label = { Text(text = "Added in ${state.supportedApi} API") },
        leadingIcon = {
            if (state is ManageMedia) return@MyTextField // I don't know how ask by it
            if (state is PackageUsageStats) return@MyTextField // I don't know how ask by it
            Icon(if (state.enabled) Rounded.Done else Rounded.Cancel, (null))
        },
        modifier = modifier
    )
}
