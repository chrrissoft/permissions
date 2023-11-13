package com.chrrissoft.permissions.ui.components

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.automirrored.rounded.Launch
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chrrissoft.permissions.Permission
import com.chrrissoft.permissions.ui.theme.inputChipBorder
import com.chrrissoft.permissions.ui.theme.inputChipColors
import com.chrrissoft.permissions.R as Resources

@Composable
fun Permission(
    state: Permission,
    onChangeItem: (Permission) -> Unit,
    modifier: Modifier = Modifier,
    onRequestResult: () -> Unit,
) {
    val launcher = run {
        rememberLauncherForActivityResult(
            contract = RequestPermission(),
            onResult = { onRequestResult() }
        )
    }

    Permission(
        uiName = state.name,
        isSupported = state.isSupported,
        checkToShow = state.checkToShow,
        supportedApi = state.supportedApi,
        isGreatPermission = state.enabled,
        disabledOnKill = state.disableOnKill,
        onLaunch = { launcher.launch(state.permission) },
        onChangeCheckToShow = { onChangeItem(state.copy(checkToShow = it)) },
        onChangeDisabledOnKill = { onChangeItem(state.copy(disableOnKill = it)) },
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Permission(
    isGreatPermission: Boolean,
    isSupported: Boolean,
    supportedApi: Int,
    uiName: String,
    disabledOnKill: Boolean,
    onChangeDisabledOnKill: (Boolean) -> Unit,
    checkToShow: Boolean,
    onChangeCheckToShow: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onLaunch: () -> Unit,
) {
    val (showPermissionUnsupported, changeShowPermissionUnsupported) = remember {
        mutableStateOf(false)
    }

    if (showPermissionUnsupported) {
        AlertDialog(
            onDismissRequest = {
                changeShowPermissionUnsupported(false)
            },
            title = {
                Text(text = "Unsupported permission")
            },
            text = {
                Text(text = stringResource(Resources.string.unsupported_permission).plus(supportedApi))
            },
            confirmButton = {
                Button(onClick = { changeShowPermissionUnsupported(false) }) {
                    Text(text = "Ok")
                }
            },
            containerColor = colorScheme.primaryContainer,
            textContentColor = colorScheme.onPrimaryContainer,
            titleContentColor = colorScheme.onPrimaryContainer,
            iconContentColor = colorScheme.onPrimaryContainer,
        )
    }

    val background = if (!isSupported) colorScheme.primaryContainer.copy((.5f))
    else colorScheme.primaryContainer
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .clickable(!isSupported) { changeShowPermissionUnsupported(true) }
            .clip(shapes.medium)
            .background(background)
            .padding(20.dp)
    ) {
        val textColor = if (!isSupported) colorScheme.secondary.copy((.5f))
        else colorScheme.onPrimaryContainer
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = uiName, color = textColor, style = typography.titleMedium)

            val greatIcon = if (isGreatPermission)
                Rounded.Done else Rounded.Cancel
            val greatIconColor = if (isSupported) {
                if (isGreatPermission)
                    colorScheme.onPrimaryContainer else colorScheme.secondary
            } else colorScheme.secondary.copy(.5f)

            Icon(imageVector = greatIcon, contentDescription = null, tint = greatIconColor)
        }
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            val (showDisableUnsupported, changeShowDisableUnsupported) = remember {
                mutableStateOf(false)
            }

            if (showDisableUnsupported) {
                AlertDialog(
                    onDismissRequest = {
                        changeShowDisableUnsupported(false)
                    },
                    title = {
                        Text(text = "Unsupported action")
                    },
                    text = {
                        Text(text = stringResource(Resources.string.unsupported_permission_action_kill))
                    },
                    confirmButton = {
                        Button(onClick = { changeShowDisableUnsupported(false) }) {
                            Text(text = "Ok")
                        }
                    },
                    containerColor = colorScheme.primaryContainer,
                    textContentColor = colorScheme.onPrimaryContainer,
                    titleContentColor = colorScheme.onPrimaryContainer,
                    iconContentColor = colorScheme.onPrimaryContainer,
                )
            }

            InputChip(
                colors = inputChipColors,
                border = inputChipBorder,
                selected = disabledOnKill,
                onClick = { onChangeDisabledOnKill(!disabledOnKill) },
                enabled = isSupported && isGreatPermission && SDK_INT >= TIRAMISU,
                label = { Text(text = "Disable on kill", style = typography.labelMedium) }
            )

            InputChip(
                selected = checkToShow,
                colors = inputChipColors,
                border = inputChipBorder,
                enabled = isSupported && !isGreatPermission,
                onClick = { onChangeCheckToShow(!checkToShow) },
                label = { Text(text = "Show in group", style = typography.labelMedium) }
            )

            FilledIconButton(
                shape = shapes.medium,
                onClick = { onLaunch() },
                enabled = isSupported && !isGreatPermission,
                content = { Icon(Icons.AutoMirrored.Rounded.Launch, (null)) },
            )
        }
    }
}
