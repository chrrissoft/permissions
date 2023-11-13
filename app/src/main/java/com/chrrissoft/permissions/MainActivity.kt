package com.chrrissoft.permissions

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.chrrissoft.permissions.common.ui.ScreenEvent
import com.chrrissoft.permissions.common.ui.ui.Screen
import com.chrrissoft.permissions.common.ui.ScreenViewModel
import com.chrrissoft.permissions.ui.components.App
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<ScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App {
                val state by viewModel.stateFlow.collectAsState()
                Screen(state = state, onEvent = { viewModel.handleEvent(it) })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.handleEvent(ScreenEvent.NormalEvent.OnEnableAsk)
        viewModel.handleEvent(ScreenEvent.OneTimeEvent.OnEnableAsk)
        viewModel.handleEvent(ScreenEvent.LocationEvent.OnEnableAsk)
        viewModel.handleEvent(ScreenEvent.SpecialEvent.OnEnableAsk)
    }

    override fun onDestroy() {
        if (SDK_INT >= TIRAMISU) {
            val permissions = viewModel.state.runTime.items
                .asSequence()
                .plus(viewModel.state.location.foreground.items)
                .plus(viewModel.state.location.background.item)
                .plus(viewModel.state.oneTime.items)
                .mapNotNull { if (it.disableOnKill && it.enabled) it.permission else null }
                .distinctBy { it }
                .toList()
            try {
                revokeSelfPermissionsOnKill(permissions)
            } catch (_: Throwable) {

            }
        }
        super.onDestroy()
    }
}
