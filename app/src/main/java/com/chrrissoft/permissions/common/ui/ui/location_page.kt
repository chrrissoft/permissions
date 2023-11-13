package com.chrrissoft.permissions.common.ui.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chrrissoft.permissions.common.ui.ScreenEvent
import com.chrrissoft.permissions.common.ui.ScreenState

@Composable
fun LocationPage(
    state: ScreenState.LocationState,
    onEvent: (ScreenEvent.LocationEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        BackgroundLocation(
            onEvent = onEvent,
            state = state.background,
            onRequestResult = {},
        )
        Spacer(modifier = Modifier.height(20.dp))
        ForegroundLocation(
            onEvent = onEvent,
            state = state.foreground,
            onRequestResult = { },
        )
        Spacer(modifier = Modifier.height(70.dp))
    }
}
