package com.chrrissoft.permissions.ui

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.Q
import android.os.PowerManager
import android.provider.Settings
import android.provider.Settings.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.ui.graphics.vector.ImageVector
import com.chrrissoft.permissions.ui.ScreenState.SpecialState.SpecialPermissionItem.Name


data class ScreenState(
    val page: Page = Page.RUN_TIME_REQUEST,
    val runTime: RunTimeRequestState = RunTimeRequestState(),
    val oneTime: OneTimeRequestState = OneTimeRequestState(),
    val location: LocationState = LocationState(),
    val special: SpecialState = SpecialState(),
) {
    enum class Page(val icon: ImageVector, val label: String) {
        RUN_TIME_REQUEST(Icons.Rounded.Favorite, "Run time"),
        ONE_TIME_RUN_TIME_REQUEST(Icons.Rounded.Favorite, "One time"),
        LOCATION(Icons.Rounded.Favorite, "Location"),
        SPECIAL(Icons.Rounded.Favorite, "Special");

        companion object {
            val pages = listOf(RUN_TIME_REQUEST, ONE_TIME_RUN_TIME_REQUEST, LOCATION, SPECIAL)
        }
    }

    data class RunTimeRequestState(val items: List<PermissionItem> = list) {
        private companion object {
            @SuppressLint("InlinedApi")
            private val list = listOf(
                Pair(ADD_VOICEMAIL, 23),
                Pair(ANSWER_PHONE_CALLS, 26),
                Pair(BLUETOOTH_SCAN, 31),
                Pair(BODY_SENSORS, 23),
                Pair(BODY_SENSORS_BACKGROUND, 33),
                Pair(CALL_PHONE, 23),
                Pair(GET_ACCOUNTS, 23),
                Pair(READ_CALENDAR, 23),
                Pair(WRITE_CALENDAR, 23),
                Pair(READ_EXTERNAL_STORAGE, 23),
                Pair(READ_MEDIA_IMAGES, 33),
                Pair(READ_MEDIA_VIDEO, 33),
                Pair(READ_MEDIA_AUDIO, 33),
                Pair(READ_PHONE_NUMBERS, 26),
                Pair(READ_PHONE_STATE, 23),
            ).map { pair ->
                if (pair.second > SDK_INT) PermissionItem(
                    name = pair.first,
                    isSupported = false,
                    uiName = pair.first.takeLastWhile { "$it"!="." },
                    supportedApi = pair.second
                )
                else PermissionItem(
                    name = pair.first,
                    isSupported = true,
                    uiName = pair.first.takeLastWhile { "$it"!="." },
                    supportedApi = pair.second
                )
            }
        }
    }

    data class OneTimeRequestState(val items: List<PermissionItem> = list) {
        private companion object {
            private val list = listOf(
                Pair(CAMERA, 23),
                Pair(RECORD_AUDIO, 23),
                Pair(ACCESS_COARSE_LOCATION, 23),
                Pair(ACCESS_FINE_LOCATION, 23),
            ).map { pair ->
                if (pair.second > SDK_INT) PermissionItem(
                    name = pair.first,
                    isSupported = false,
                    uiName = pair.first.takeLastWhile { "$it"!="." },
                    supportedApi = pair.second
                )
                else PermissionItem(
                    name = pair.first,
                    isSupported = true,
                    uiName = pair.first.takeLastWhile { "$it"!="." },
                    supportedApi = pair.second
                )
            }
        }
    }

    data class LocationState(
        val background: Background = Background(),
        val foreground: Foreground = Foreground(),
    ) {
        data class Background(val item: PermissionItem = defaultItem) {
            private companion object {
                @SuppressLint("InlinedApi")
                private val defaultItem = PermissionItem(
                    supportedApi = 29,
                    isSupported = SDK_INT >= Q,
                    name = ACCESS_BACKGROUND_LOCATION,
                    uiName = "ACCESS_BACKGROUND_LOCATION",
                )
            }
        }

        data class Foreground(val items: List<PermissionItem> = list) {
            private companion object {
                @SuppressLint("InlinedApi")
                private val list = listOf(
                    Pair(ACCESS_COARSE_LOCATION, 23),
                    Pair(ACCESS_FINE_LOCATION, 23),
                ).map { pair ->
                    if (pair.second > SDK_INT) PermissionItem(
                        name = pair.first,
                        isSupported = false,
                        uiName = pair.first.takeLastWhile { "$it"!="." },
                        supportedApi = pair.second
                    )
                    else PermissionItem(
                        name = pair.first,
                        isSupported = true,
                        uiName = pair.first.takeLastWhile { "$it"!="." },
                        supportedApi = pair.second
                    )
                }
            }
        }
    }

    data class SpecialState(val items: List<SpecialPermissionItem> = list, val foo: Any? = null) {

        private companion object {
            @SuppressLint("InlinedApi", "BatteryLife")
            val list = listOf(
                Triple(Name.MANAGE_MEDIA, (31), ACTION_REQUEST_MANAGE_MEDIA),
                Triple(Name.WRITE_SETTINGS, (23), ACTION_MANAGE_WRITE_SETTINGS),
                Triple(Name.SYSTEM_ALERT_WINDOW, (23), ACTION_MANAGE_OVERLAY_PERMISSION),
                Triple(Name.SCHEDULE_EXACT_ALARM, (31), ACTION_REQUEST_SCHEDULE_EXACT_ALARM),
                Triple(Name.DO_NOT_DISTURB_MODE, (23), ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS),
                Triple(Name.MANAGE_EXTERNAL_STORAGE, (30), ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION),
                Triple(Name.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, (23), ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS),
            ).map { tripe ->
                val isSupported = SDK_INT >= tripe.second
                SpecialPermissionItem(
                    name = tripe.first,
                    isSupported = isSupported,
                    supportedApi = tripe.second,
                    actionToLaunch = tripe.third,
                )
            }
        }

        data class SpecialPermissionItem(
            val name: Name,
            val supportedApi: Int,
            val isSupported: Boolean,
            val actionToLaunch: String,

        ) {
            init {

            }
            enum class Name {
                WRITE_SETTINGS,
                MANAGE_MEDIA,
                SYSTEM_ALERT_WINDOW,
                SCHEDULE_EXACT_ALARM,
                DO_NOT_DISTURB_MODE,
                MANAGE_EXTERNAL_STORAGE,
                REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
            }
        }
    }

    data class PermissionItem(
        val uiName: String,
        val name: String,
        val isSupported: Boolean,
        val checkToShow: Boolean = false,
        val disableOnKill: Boolean = false,
        val supportedApi: Int,
    )
}
