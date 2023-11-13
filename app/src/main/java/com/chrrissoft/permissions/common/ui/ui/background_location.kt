package com.chrrissoft.permissions.common.ui.ui

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chrrissoft.permissions.common.ui.ScreenEvent
import com.chrrissoft.permissions.common.ui.ScreenState
import com.chrrissoft.permissions.ui.components.Permission
import com.chrrissoft.permissions.ui.components.SectionTitle

@Composable
fun BackgroundLocation(
    state: ScreenState.LocationState.Background,
    onEvent: (ScreenEvent.LocationEvent.BackgroundEvent) -> Unit,
    onRequestResult: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SectionTitle("Background")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Text(
                text = "Android version do not support\nbackground location permission 😫",
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shapes.small)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 10.dp)
                    .padding(vertical = 15.dp)
            )
            return
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            shape = shapes.medium,
            enabled = state.item.enabled,
            onClick = { onEvent(ScreenEvent.LocationEvent.BackgroundEvent.OnStartBackgroundLocationWork) },
            content = { Text(text = "Launch background work location") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Permission(
            state = state.item,
            onRequestResult = onRequestResult,
            onChangeItem = { onEvent(ScreenEvent.LocationEvent.BackgroundEvent.OnChangePermission(it)) },
        )
    }
}
