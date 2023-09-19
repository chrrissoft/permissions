package com.chrrissoft.permissions

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.chrrissoft.permissions.ui.Screen
import com.chrrissoft.permissions.ui.ScreenViewModel
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
        viewModel.updateSpecialsState(foo = Any())
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.revokeSelfPermissionsOnKill(viewModel.state.runTime.items.map { it.name })
        }
    }
}
