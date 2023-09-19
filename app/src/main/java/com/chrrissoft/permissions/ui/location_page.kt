package com.chrrissoft.permissions.ui

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.os.Build.VERSION_CODES.Q
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import com.chrrissoft.permissions.Util.isGreatCoarseLocation
import com.chrrissoft.permissions.Util.isGreatFineLocation
import com.chrrissoft.permissions.location.ForegroundLocationPermissionService
import com.chrrissoft.permissions.ui.ScreenEvent.LocationEvent.BackgroundEvent
import com.chrrissoft.permissions.ui.ScreenEvent.LocationEvent.BackgroundEvent.OnStartBackgroundLocationWork
import com.chrrissoft.permissions.ui.ScreenEvent.LocationEvent.ForegroundEvent
import com.chrrissoft.permissions.ui.ScreenState.LocationState.Background
import com.chrrissoft.permissions.ui.ScreenState.LocationState.Foreground
import com.chrrissoft.permissions.ui.components.PermissionItem
import com.chrrissoft.permissions.ui.components.SectionTitle
import com.chrrissoft.permissions.ui.ScreenEvent.LocationEvent.BackgroundEvent.OnChangePermissionItem as OnChangePermissionItemBackground
import com.chrrissoft.permissions.ui.ScreenEvent.LocationEvent.ForegroundEvent.OnChangePermissionItem as OnChangePermissionItemForeground

@Composable
fun LocationPage(
    state: ScreenState.LocationState,
    onEvent: (ScreenEvent.LocationEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        val ctx = LocalContext.current
        var backgroundLauncherEnabled by remember(state) {
            mutableStateOf(ctx.isGreatFineLocation || ctx.isGreatCoarseLocation)
        }
        Spacer(modifier = Modifier.height(10.dp))
        BackgroundLocation(
            state = state.background,
            onEvent = onEvent,
            launchEnabled = backgroundLauncherEnabled
        )
        Spacer(modifier = Modifier.height(20.dp))
        ForegroundLocation(
            state = state.foreground,
            onEvent = onEvent,
            onUserResponse = {
                backgroundLauncherEnabled = it
            }
        )
    }
}

@Composable
private fun BackgroundLocation(
    state: Background,
    onEvent: (BackgroundEvent) -> Unit,
    launchEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SectionTitle("Background")
        if (SDK_INT < Q) {
            Text(
                text = "Android version do not support\nbackground location permission 😫",
                color = colorScheme.onPrimary,
                textAlign = Center,
                style = typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shapes.large)
                    .background(colorScheme.primary)
                    .padding(horizontal = 10.dp)
                    .padding(vertical = 15.dp)
            )
            return
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { onEvent(OnStartBackgroundLocationWork) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Launch background work location")
        }
        Spacer(modifier = Modifier.height(10.dp))
        PermissionItem(
            item = state.item,
            onChangeItem = {
                onEvent(OnChangePermissionItemBackground(it))
            },
            launchEnabled = launchEnabled,
        )
    }
}

@Composable
private fun ForegroundLocation(
    state: Foreground,
    onEvent: (ForegroundEvent) -> Unit,
    onUserResponse: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val ctx = LocalContext.current
    Column(modifier = modifier) {
        SectionTitle("Foreground")
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                Intent(ctx, ForegroundLocationPermissionService::class.java).apply {
                    if (SDK_INT >= O) ctx.startForegroundService(this)
                    else ctx.startService(this)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Launch foreground service location")
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn {
            items(state.items) { item ->
                PermissionItem(
                    item = item,
                    onChangeItem = {
                        onEvent(OnChangePermissionItemForeground(it))
                    },
                    onUserResponse = onUserResponse
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
