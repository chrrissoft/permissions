package com.chrrissoft.permissions.common.ui.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chrrissoft.permissions.common.ui.ScreenState
import com.chrrissoft.permissions.common.ui.ScreenEvent
import com.chrrissoft.permissions.common.ui.ScreenEvent.NormalEvent.OnChangePermission
import com.chrrissoft.permissions.common.ui.ScreenEvent.NormalEvent.OnEnableAsk
import com.chrrissoft.permissions.ui.components.Permission

@Composable
fun NormalPage(
    state: ScreenState.NormalState,
    onEvent: (ScreenEvent.NormalEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(state.items) { item ->
            Permission(
                state = item,
                onRequestResult = { onEvent(OnEnableAsk) },
                onChangeItem = { onEvent(OnChangePermission(it)) },
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        item { Spacer(modifier = Modifier.height(70.dp)) }
    }
}
