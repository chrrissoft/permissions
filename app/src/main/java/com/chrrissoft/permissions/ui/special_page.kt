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
import com.chrrissoft.permissions.ui.ScreenEvent.SpecialEvent
import com.chrrissoft.permissions.ui.ScreenState.SpecialState
import com.chrrissoft.permissions.ui.components.SpecialPermissionItem

@Composable
fun SpecialPage(
    state: SpecialState,
    onEvent: (SpecialEvent) -> Unit,
    modifier: Modifier
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
            SpecialPermissionItem(item = item, any = state.foo)
            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            Spacer(modifier = Modifier.height(70.dp))
        }
    }
}
