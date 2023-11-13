package com.chrrissoft.permissions.custom.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import com.chrrissoft.permissions.ui.components.App

abstract class PermissionActivity(private val title: String) : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App { Box(Modifier.fillMaxSize(), Center) { Text(text = "$title activity") } }
        }
    }

    class Normal0 : PermissionActivity(title = "Normal 0")
    class Normal1 : PermissionActivity(title = "Normal 1")
    class Normal2 : PermissionActivity(title = "Normal 2")


    class RunTime0 : PermissionActivity(title = "Run time 0")
    class RunTime1 : PermissionActivity(title = "Run time 1")
    class RunTime2 : PermissionActivity(title = "Run time 2")

    class Signature0 : PermissionActivity(title = "Signature 0")
    class Signature1 : PermissionActivity(title = "Signature 1")
    class Signature2 : PermissionActivity(title = "Signature 2")
}
