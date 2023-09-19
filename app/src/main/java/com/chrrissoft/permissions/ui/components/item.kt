package com.chrrissoft.permissions.ui.components

import android.Manifest.permission.*
import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.*
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Launch
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chrrissoft.permissions.R as Resources
import com.chrrissoft.permissions.Util.isPermissionGreat
import com.chrrissoft.permissions.ui.ScreenState.PermissionItem
import com.chrrissoft.permissions.ui.ScreenState.SpecialState.SpecialPermissionItem
import com.chrrissoft.permissions.ui.ScreenState.SpecialState.SpecialPermissionItem.Name

@Composable
fun SpecialPermissionItem(
    item: SpecialPermissionItem,
    any: Any?,
    modifier: Modifier = Modifier,
    launchEnabled: Boolean? = null,
) {
    val ctx = LocalContext.current
    val alarmManager = remember {
        ctx.getSystemService(AlarmManager::class.java)
    }

    val notificationManager = remember {
        ctx.getSystemService(NotificationManager::class.java)
    }

    val isGreatPermission = remember(any) {
        when (item.name) {
            Name.WRITE_SETTINGS -> Settings.System.canWrite(ctx)
            Name.MANAGE_MEDIA -> {
                if (SDK_INT >= S) ctx.isPermissionGreat(MANAGE_MEDIA) else false
            }
            Name.SYSTEM_ALERT_WINDOW -> Settings.canDrawOverlays(ctx)
            Name.SCHEDULE_EXACT_ALARM -> {
                if (SDK_INT >= S) alarmManager.canScheduleExactAlarms() else true
            }
            Name.DO_NOT_DISTURB_MODE -> {
                notificationManager.isNotificationPolicyAccessGranted
            }
            Name.MANAGE_EXTERNAL_STORAGE -> {
                if (SDK_INT >= R) ctx.isPermissionGreat(MANAGE_EXTERNAL_STORAGE) else false
            }
            Name.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS -> {
                ctx.isPermissionGreat(REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            }
        }
    }

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
                Text(text = stringResource(Resources.string.unsupported_permission).plus(item.supportedApi))
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

    val (showError, changeShowError) = remember {
        mutableStateOf((false))
    }

    if (showError) {
        AlertDialog(
            onDismissRequest = {
                changeShowError(false)
            },
            title = {
                Text(text = "Some Error")
            },
            text = {
                Text(text = "Some error was occurred, probably an activity was not found error")
            },
            confirmButton = {
                Button(onClick = { changeShowError(false) }) {
                    Text(text = "Ok")
                }
            },
            containerColor = colorScheme.errorContainer,
            textContentColor = colorScheme.onErrorContainer,
            titleContentColor = colorScheme.onErrorContainer,
            iconContentColor = colorScheme.onErrorContainer,
        )
    }

    PermissionItem(
        isGreatPermission = isGreatPermission,
        isSupported = item.isSupported,
        supportedApi = item.supportedApi,
        uiName = "${item.name}",
        disabledOnKill = false,
        disabledOnKillEnabled = false,
        onChangeDisabledOnKill = {},
        checkToShow = false,
        checkToShowEnabled = false,
        onChangeCheckToShow = {},
        onLaunch = {
            try {
                Intent(item.actionToLaunch).apply {
                    if (item.name != Name.DO_NOT_DISTURB_MODE) {
                        data = Uri.parse("package:com.chrrissoft.permissions")
                    }
                    ctx.startActivity((this))
                }
            } catch (e: Exception) {
                changeShowError(true)
            }
        },
        launchEnabled = launchEnabled,
        modifier = modifier
    )
}


@Composable
fun PermissionItem(
    item: PermissionItem,
    onChangeItem: (PermissionItem) -> Unit,
    modifier: Modifier = Modifier,
    launchEnabled: Boolean? = null,
    onUserResponse: ((Boolean) -> Unit)? = null
) {
    val ctx = LocalContext.current
    var isGreatPermission by remember(item) {
        mutableStateOf(ctx.isPermissionGreat(item.name))
    }

    val launcher = run {
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = {
                debug(it)
                isGreatPermission = it
                if (it) {
                    onChangeItem(item.copy(checkToShow = false))
                }
                onUserResponse?.invoke(it)
            }
        )
    }

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
                Text(text = stringResource(Resources.string.unsupported_permission).plus(item.supportedApi))
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


    PermissionItem(
        isGreatPermission = isGreatPermission,
        isSupported = item.isSupported,
        supportedApi = item.supportedApi,
        uiName = item.uiName,
        disabledOnKill = item.disableOnKill,
        onChangeDisabledOnKill = {
            onChangeItem(item.copy(disableOnKill = it))
        },
        checkToShow = item.checkToShow,
        onChangeCheckToShow = {
            onChangeItem(item.copy(checkToShow = it))
        },
        launchEnabled = launchEnabled,
        onLaunch = {
            launcher.launch(item.name)
        },
        modifier = modifier,
        disabledOnKillEnabled = false
    )
}


@Composable
fun PermissionItem(
    isGreatPermission: Boolean,
    isSupported: Boolean,
    supportedApi: Int,
    uiName: String,
    disabledOnKill: Boolean,
    onChangeDisabledOnKill: (Boolean) -> Unit,
    checkToShow: Boolean,
    onChangeCheckToShow: (Boolean) -> Unit,
    launchEnabled: Boolean? = null,
    checkToShowEnabled: Boolean? = null,
    onLaunch: () -> Unit,
    modifier: Modifier = Modifier,
    disabledOnKillEnabled: Boolean? = null,
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
            .clip(MaterialTheme.shapes.medium)
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
                Icons.Rounded.Done else Icons.Rounded.Cancel
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

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .clickable(disabledOnKillEnabled ?: true) {
                        if (isSupported && SDK_INT >= TIRAMISU) {
                            onChangeDisabledOnKill(!disabledOnKill)
                        } else {
                            changeShowDisableUnsupported(true)
                        }
                    }
            ) {
                val color =
                    if (isSupported && SDK_INT >= TIRAMISU) {
                        if (disabledOnKill) colorScheme.onPrimaryContainer
                        else colorScheme.secondary
                    } else colorScheme.secondary.copy(.5f)

                Text(
                    text = "Disable on kill",
                    style = typography.labelMedium.copy(color = color)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Checkbox(
                    enabled = disabledOnKillEnabled ?: isSupported && SDK_INT >= TIRAMISU,
                    checked = disabledOnKill,
                    onCheckedChange = onChangeDisabledOnKill
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .clickable(enabled = checkToShowEnabled ?: isSupported && !isGreatPermission) {
                        onChangeCheckToShow(!checkToShow)
                    }
            ) {
                val color = if (isSupported && !isGreatPermission) {
                    if (checkToShow) colorScheme.onPrimaryContainer
                    else colorScheme.secondary
                } else colorScheme.secondary.copy(.5f)

                Text(
                    text = "Show in group",
                    style = typography.labelMedium.copy(color = color)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Checkbox(
                    enabled = checkToShowEnabled ?: isSupported && !isGreatPermission,
                    checked = checkToShow,
                    onCheckedChange = onChangeCheckToShow
                )
            }

            FilledIconButton(
                enabled = launchEnabled ?: isSupported && !isGreatPermission,
                onClick = { onLaunch() },
                colors = run {
                    IconButtonDefaults.filledIconButtonColors(
                        contentColor = colorScheme.primary,
                        containerColor = colorScheme.onPrimary,
                    )
                }
            ) {
                Icon(imageVector = Icons.Rounded.Launch, contentDescription = null)
            }
        }
    }
}

private fun debug(message: Any?) {
    Log.d(TAG, "$message")
}

private const val TAG = "PermissionItem"
