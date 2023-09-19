package com.chrrissoft.permissions.ui

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Launch
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import com.chrrissoft.permissions.Util.setBarsColors
import com.chrrissoft.permissions.ui.ScreenEvent.LocationEvent.BackgroundEvent
import com.chrrissoft.permissions.ui.ScreenEvent.OnChangePage
import com.chrrissoft.permissions.ui.ScreenState.Page.*
import com.chrrissoft.permissions.ui.ScreenState.Page.Companion.pages
import com.chrrissoft.permissions.ui.theme.centerAlignedTopAppBarColors
import com.chrrissoft.permissions.ui.theme.navigationBarItemColors
import com.chrrissoft.permissions.ui.ScreenEvent.LocationEvent.ForegroundEvent.OnChangeCheckToShowByName as LocationForeground
import com.chrrissoft.permissions.ui.ScreenEvent.OneTimeRunTimeRequestEvent.OnChangeCheckToShowByName as OneTime
import com.chrrissoft.permissions.ui.ScreenEvent.RunTimeRequestEvent.OnChangeCheckToShowByName as RunTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen(
    state: ScreenState,
    onEvent: (ScreenEvent) -> Unit,
) {
    setBarsColors()

    val multiPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {
            it.forEach { result ->
                debug(result)
                if (result.value) {
                    when (state.page) {
                        RUN_TIME_REQUEST -> {
                            onEvent(RunTime(name = result.key, check = false))
                        }
                        ONE_TIME_RUN_TIME_REQUEST -> {
                            onEvent(OneTime(name = result.key, check = false))
                        }
                        LOCATION -> {
                            if (result.key==ACCESS_BACKGROUND_LOCATION) {
                                val back = state.location.background.item.copy(checkToShow = false)
                                onEvent(BackgroundEvent.OnChangePermissionItem(back))
                            }
                            onEvent(LocationForeground(result.key, false))
                        }
                        SPECIAL -> TODO()
                    }
                }
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
            ExtendedFloatingActionButton(
                onClick = {
                    val list = when (state.page) {
                        RUN_TIME_REQUEST -> {
                            state.runTime.items.mapNotNull {
                                if (it.checkToShow) it.name else null
                            }
                        }
                        ONE_TIME_RUN_TIME_REQUEST -> {
                            state.oneTime.items.mapNotNull {
                                if (it.checkToShow) it.name else null
                            }
                        }
                        LOCATION -> {
                            val backItem = state.location.background.item
                            val back = if (backItem.checkToShow) backItem.name else null
                            state.location.foreground.items.map {
                                if (it.checkToShow) it.name else null
                            }
                                .plus(back)
                                .mapNotNull { it }
                        }
                        SPECIAL -> throw IllegalArgumentException()
                    }
                    multiPermissionLauncher.launch(list.toTypedArray())
                }
            ) {
                val text = remember(state.page) {
                    when (state.page) {
                        RUN_TIME_REQUEST, ONE_TIME_RUN_TIME_REQUEST, LOCATION -> "Ask for the selected"
                        SPECIAL -> "TODO"
                    }
                }
                Text(text = text)
                Spacer(modifier = Modifier.width(10.dp))

                val icon = remember(state.page) {
                    when (state.page) {
                        RUN_TIME_REQUEST, ONE_TIME_RUN_TIME_REQUEST, LOCATION -> Icons.Rounded.Launch
                        SPECIAL -> Icons.Rounded.Favorite
                    }
                }
                Icon(imageVector = icon, contentDescription = null)
            }
        },
        bottomBar = {
            NavigationBar(
                contentColor = colorScheme.primary,
                containerColor = colorScheme.primaryContainer,
            ) {
                pages.forEach {
                    NavigationBarItem(
                        selected = state.page==it,
                        onClick = { onEvent(OnChangePage(it)) },
                        label = { Text(text = it.label) },
                        colors = navigationBarItemColors,
                        icon = { Icon(imageVector = it.icon, contentDescription = null) },
                    )
                }
            }
        },
        containerColor = colorScheme.onPrimary,
        content = {
            ScreenContent(
                state = state,
                onEvent = onEvent,
                modifier = Modifier.padding(it)
            )
        },
    )
}

@Composable
private fun ScreenContent(
    state: ScreenState,
    onEvent: (ScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    when (state.page) {
        RUN_TIME_REQUEST -> {
            RunTimeRequestPage(
                state = state.runTime,
                onEvent = onEvent,
                modifier = modifier
            )
        }
        ONE_TIME_RUN_TIME_REQUEST -> {
            OneTimeRunTimeRequestPage(
                state = state.oneTime,
                onEvent = onEvent,
                modifier = modifier
            )
        }
        LOCATION -> {
            LocationPage(
                state = state.location,
                onEvent = onEvent,
                modifier = modifier
            )
        }
        SPECIAL -> {
            SpecialPage(
                state = state.special,
                onEvent = onEvent,
                modifier = modifier
            )
        }
    }
}

private fun debug(message: Any?) {
    Log.d(TAG, "$message")
}

private const val TAG = "Screen"
