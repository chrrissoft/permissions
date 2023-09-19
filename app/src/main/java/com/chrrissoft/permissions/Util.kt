package com.chrrissoft.permissions

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.Q
import android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
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

    fun Context.isPermissionGreat(permission: String): Boolean {
        return checkSelfPermission(permission)==PERMISSION_GRANTED
    }

    val Context.isGreatCamera: Boolean
        get() = run {
            isPermissionGreat(android.Manifest.permission.CAMERA)
        }

    val Context.isGreatMic: Boolean
        get() = run {
            isPermissionGreat(android.Manifest.permission.RECORD_AUDIO)
        }

    val Context.isGreatCoarseLocation: Boolean
        get() = run {
            isPermissionGreat(ACCESS_COARSE_LOCATION)
        }

    val Context.isGreatFineLocation: Boolean
        get() = run {
            isPermissionGreat(ACCESS_FINE_LOCATION)
        }

    val Context.isGreatBackgroundLocation: Boolean
        get() = run {
            if (SDK_INT >= Q) isPermissionGreat(ACCESS_BACKGROUND_LOCATION) else true
        }
}
