package com.chrrissoft.permissions.common.ui.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chrrissoft.permissions.common.ui.ScreenEvent
import com.chrrissoft.permissions.common.ui.ScreenState
import com.chrrissoft.permissions.common.ui.ScreenState.Page.*

@Composable
fun ScreenContent(
    state: ScreenState,
    onEvent: (ScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    when (state.page) {
        NORMAL -> NormalPage(state = state.runTime, onEvent = onEvent, modifier = modifier)
        ONE_TIME -> OneTimePage(state = state.oneTime, onEvent = onEvent, modifier = modifier)
        LOCATION -> LocationPage(state = state.location, onEvent = onEvent, modifier = modifier)
        SPECIAL -> SpecialPage(state = state.special, onEvent = onEvent, modifier = modifier)
    }
}
