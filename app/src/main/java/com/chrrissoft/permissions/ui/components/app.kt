package com.chrrissoft.permissions.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chrrissoft.permissions.ui.theme.PermissionsTheme

@Composable
fun App(app: @Composable () -> Unit) {
    PermissionsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) { app() }
    }
}
