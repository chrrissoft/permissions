package com.chrrissoft.permissions.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chrrissoft.permissions.ui.ScreenEvent.RunTimeRequestEvent.OnChangePermissionItem
import com.chrrissoft.permissions.ui.components.PermissionItem

@Composable
fun RunTimeRequestPage(
    state: ScreenState.RunTimeRequestState,
    onEvent: (ScreenEvent.RunTimeRequestEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
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
