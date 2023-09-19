package com.chrrissoft.permissions.ui

import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chrrissoft.permissions.ui.ScreenEvent.OneTimeRunTimeRequestEvent.OnChangePermissionItem
import com.chrrissoft.permissions.ui.components.PermissionItem
import com.chrrissoft.permissions.OneTimePermissionForegroundService as Service

@Composable
fun OneTimeRunTimeRequestPage(
    state: ScreenState.OneTimeRequestState,
    onEvent: (ScreenEvent.OneTimeRunTimeRequestEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (SDK_INT < R) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Center) {
            Text(
                color = colorScheme.primary,
                text = "Android version do not\nsupport this feature 😫",
            )
        }
        return
    }

    val ctx = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                // see import alias of Service
                val intent = Intent(ctx, Service::class.java)
                ctx.startService(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Start foreground service")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "You should choose \"Only this time\"",
            color = colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            style = typography.labelSmall,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shapes.large)
                .background(colorScheme.primary)
                .padding(5.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn {
            items(state.items) { item ->
                PermissionItem(
                    item = item,
                    onChangeItem = {
                        onEvent(OnChangePermissionItem(it))
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                Spacer(modifier = Modifier.height(70.dp))
            }
        }
    }
}
