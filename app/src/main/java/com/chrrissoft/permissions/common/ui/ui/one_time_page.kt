package com.chrrissoft.permissions.common.ui.ui

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.Q
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.chrrissoft.permissions.common.ui.ScreenState
import com.chrrissoft.permissions.common.ui.ScreenEvent
import com.chrrissoft.permissions.common.ui.ScreenEvent.OneTimeEvent.OnChangePermission
import com.chrrissoft.permissions.common.ui.ScreenEvent.OneTimeEvent.OnEnableAsk
import com.chrrissoft.permissions.ui.components.Permission
import com.chrrissoft.permissions.common.app.OneTimePermissionForegroundService as Service

@Composable
fun OneTimePage(
    state: ScreenState.OneTimeState,
    onEvent: (ScreenEvent.OneTimeEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (SDK_INT < Q) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Center) {
            Text(
                color = colorScheme.primary,
                text = "Android version do not\nsupport this feature 😫",
            )
        }
        return
    }

    val ctx = LocalContext.current
    Column(modifier = modifier.fillMaxWidth()) {
        Button(
            shape = shapes.medium,
            content = { Text(text = "Start foreground service") },
            onClick = { Intent(ctx, Service::class.java).let { ctx.startService(it) } },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn {
            items(state.items) { item ->
                Permission(
                    state = item,
                    onRequestResult = { onEvent(OnEnableAsk) },
                    onChangeItem = { onEvent(OnChangePermission(it)) },
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                Spacer(modifier = Modifier.height(70.dp))
            }
        }
    }
}
