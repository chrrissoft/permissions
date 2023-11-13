package com.chrrissoft.permissions.common.ui.ui

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.chrrissoft.permissions.Util.startForegroundCompat
import com.chrrissoft.permissions.common.ui.ScreenEvent
import com.chrrissoft.permissions.common.ui.ScreenEvent.LocationEvent.ForegroundEvent.OnChangePermission
import com.chrrissoft.permissions.common.ui.ScreenState
import com.chrrissoft.permissions.ui.components.Permission
import com.chrrissoft.permissions.ui.components.SectionTitle
import com.chrrissoft.permissions.common.app.ForegroundLocationPermissionService as Service

@Composable
fun ForegroundLocation(
    state: ScreenState.LocationState.Foreground,
    onEvent: (ScreenEvent.LocationEvent.ForegroundEvent) -> Unit,
    onRequestResult: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ctx = LocalContext.current
    Column(modifier = modifier) {
        SectionTitle("Foreground")
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            shape = shapes.medium,
            enabled = state.items.any { it.enabled },
            content = { Text(text = "Launch foreground service location") },
            onClick = { Intent(ctx, Service::class.java).let { ctx.startForegroundCompat(it) } },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn {
            items(state.items) { item ->
                Permission(
                    state = item,
                    onChangeItem = {
                        onEvent(OnChangePermission(it))
                    },
                    onRequestResult = onRequestResult,
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
