package com.chrrissoft.permissions.common.ui.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chrrissoft.permissions.common.ui.ScreenEvent.SpecialEvent
import com.chrrissoft.permissions.common.ui.ScreenState.SpecialState
import com.chrrissoft.permissions.ui.components.SpecialPermission

@Composable
fun SpecialPage(
    state: SpecialState,
    onEvent: (SpecialEvent) -> Unit,
    modifier: Modifier,
) {
    LaunchedEffect(Unit) { onEvent(SpecialEvent.OnEnableAsk) }
    LazyColumn(modifier = modifier) {
        items(state.items) { item ->
            SpecialPermission(state = item)
            Spacer(modifier = Modifier.height(10.dp))
        }
        item { Spacer(modifier = Modifier.height(70.dp)) }
    }
}
