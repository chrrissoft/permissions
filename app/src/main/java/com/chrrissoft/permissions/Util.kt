@file:Suppress("DEPRECATION")

package com.chrrissoft.permissions

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.os.Build.VERSION_CODES.S
import android.os.Parcelable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.core.app.NotificationCompat.Builder
import com.chrrissoft.permissions.R.drawable.ic_launcher_foreground
import com.chrrissoft.permissions.R.mipmap.ic_launcher
import com.google.accompanist.systemuicontroller.rememberSystemUiController

object Util {
    @SuppressLint("ComposableNaming")
    @Composable
    fun setBarsColors(
        status: Color = colorScheme.primaryContainer,
        bottom: Color = colorScheme.primaryContainer,
    ) {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = !isSystemInDarkTheme()

        DisposableEffect(systemUiController, useDarkIcons) {
            systemUiController.setStatusBarColor(status, useDarkIcons)
            systemUiController.setNavigationBarColor(bottom)
            onDispose {}
        }
    }

    fun Context.hasPermission(permission: String): Boolean {
        return checkSelfPermission(permission) == PERMISSION_GRANTED
    }

    fun generalNotification(
        ctx: Context,
        title: String,
        chanel: String = Constants.GENERAL_CHANNEL_ID,
        text: String? = null,
        subText: String? = null,
    ) : Builder {
        val largeIcon = BitmapFactory.decodeResource(ctx.resources, ic_launcher_foreground)
        return Builder(ctx, chanel)
            .setContentTitle(title)
            .setSubText(subText)
            .setContentText(text)
            .setSmallIcon(ic_launcher)
            .setLargeIcon(largeIcon)
    }

    fun AlarmManager.canScheduleExactAlarmsCompat(): Boolean {
        return if (SDK_INT >= S) canScheduleExactAlarms() else true
    }

    inline fun<reified T : Parcelable> Intent.getParcelableCompat(key: String): T? {
        return if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) getParcelableExtra(key, T::class.java)
        else getParcelableExtra(key)
    }

    fun Context.startForegroundCompat(intent: Intent) {
        if (SDK_INT >= O) startForegroundService(intent)
        else startService(intent)
    }
}
