package com.chrrissoft.permissions.common.ui.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Launch
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import com.chrrissoft.permissions.Util.setBarsColors
import com.chrrissoft.permissions.common.ui.ScreenEvent
import com.chrrissoft.permissions.common.ui.ScreenEvent.OnChangePage
import com.chrrissoft.permissions.common.ui.ScreenState
import com.chrrissoft.permissions.common.ui.ScreenState.Page.*
import com.chrrissoft.permissions.common.ui.ScreenState.Page.Companion.pages
import com.chrrissoft.permissions.ui.theme.centerAlignedTopAppBarColors
import com.chrrissoft.permissions.ui.theme.navigationBarItemColors


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen(
    state: ScreenState,
    onEvent: (ScreenEvent) -> Unit,
) {
    setBarsColors()

    val multiPermissionLauncher = rememberLauncherForActivityResult(
        contract = RequestMultiplePermissions(),
        onResult = {
            when (state.page) {
                NORMAL -> onEvent(ScreenEvent.NormalEvent.OnEnableAsk)
                ONE_TIME -> onEvent(ScreenEvent.OneTimeEvent.OnEnableAsk)
                LOCATION -> onEvent(ScreenEvent.LocationEvent.OnEnableAsk)
                SPECIAL -> {}
            }
        }
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = centerAlignedTopAppBarColors,
                title = { Text(text = "Permissions App", fontWeight = Bold) }
            )
        },
        floatingActionButton = {
            if (state.page == SPECIAL) return@Scaffold
            ExtendedFloatingActionButton(
                onClick = {
                    val list = when (state.page) {
                        NORMAL -> state.runTime.items
                        ONE_TIME -> state.oneTime.items
                        LOCATION -> state.location.foreground.items
                            .plus(state.location.background.item)
                        else -> throw IllegalArgumentException()
                    }
                        .mapNotNull { if (it.checkToShow) it.permission else null }
                    multiPermissionLauncher.launch(list.toTypedArray())
                }
            ) {
                Text(text = "Ask for the selected")
                Spacer(Modifier.width(10.dp))
                Icon(Icons.AutoMirrored.Rounded.Launch, (null))
            }
        },
        bottomBar = {
            NavigationBar(
                contentColor = colorScheme.primary,
                containerColor = colorScheme.primaryContainer,
            ) {
                pages.forEach {
                    NavigationBarItem(
                        selected = state.page == it,
                        colors = navigationBarItemColors,
                        onClick = { onEvent(OnChangePage(it)) },
                        icon = { Icon(imageVector = it.icon, contentDescription = null) },
                        label = if (state.page == it) {
                            { Text(text = it.label) }
                        } else null,
                    )
                }
            }
        },
        containerColor = colorScheme.onPrimary,
        content = {
            ScreenContent(
                state = state,
                onEvent = onEvent,
                modifier = Modifier
                    .padding(it)
                    .padding(10.dp)
            )
        },
    )
}
