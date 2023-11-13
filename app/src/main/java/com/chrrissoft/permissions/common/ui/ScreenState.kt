package com.chrrissoft.permissions.common.ui

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.provider.Settings.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.ui.graphics.vector.ImageVector
import com.chrrissoft.permissions.Permission
import com.chrrissoft.permissions.common.app.SpecialPermission
import com.chrrissoft.permissions.common.app.SpecialPermission.Companion.permissions

@SuppressLint("InlinedApi")
data class ScreenState(
    val page: Page = Page.NORMAL,
    val runTime: NormalState = NormalState(),
    val oneTime: OneTimeState = OneTimeState(),
    val location: LocationState = LocationState(),
    val special: SpecialState = SpecialState(),
) {
    enum class Page(val icon: ImageVector, val label: String) {
        NORMAL(Icons.Rounded.Favorite, "Run time"),
        ONE_TIME(Icons.Rounded.Favorite, "One time"),
        LOCATION(Icons.Rounded.Favorite, "Location"),
        SPECIAL(Icons.Rounded.Favorite, "Special");

        companion object {
            val pages = listOf(NORMAL, ONE_TIME, LOCATION, SPECIAL)
        }
    }

    data class NormalState(val items: List<Permission> = list) {
        private companion object {
            private val list = listOf(
                Permission(ADD_VOICEMAIL, (23)),
                Permission(ANSWER_PHONE_CALLS, (26)),
                Permission(BLUETOOTH_SCAN, (31)),
                Permission(BODY_SENSORS, (23)),
                Permission(BODY_SENSORS_BACKGROUND, (33)),
                Permission(CALL_PHONE, (23)),
                Permission(GET_ACCOUNTS, (23)),
                Permission(READ_CALENDAR, (23)),
                Permission(WRITE_CALENDAR, (23)),
                Permission(READ_EXTERNAL_STORAGE, (23)),
                Permission(MANAGE_EXTERNAL_STORAGE, (30)),
                Permission(ACCESS_MEDIA_LOCATION, (29)),
                Permission(READ_MEDIA_IMAGES, (33)),
                Permission(READ_MEDIA_VIDEO, (33)),
                Permission(READ_MEDIA_AUDIO, (33)),
                Permission(READ_PHONE_NUMBERS, (26)),
                Permission(READ_PHONE_STATE, (23)),
            )
        }
    }

    data class OneTimeState(val items: List<Permission> = list) {
        private companion object {
            private val list = listOf(
                Permission(CAMERA, (23)),
                Permission(RECORD_AUDIO, (23)),
                Permission(ACCESS_COARSE_LOCATION, (23)),
                Permission(ACCESS_FINE_LOCATION, (23)),
            )
        }
    }

    data class LocationState(
        val background: Background = Background(),
        val foreground: Foreground = Foreground(),
    ) {
        data class Background(val item: Permission = Permission(ACCESS_BACKGROUND_LOCATION, (29)))

        data class Foreground(val items: List<Permission> = list) {
            private companion object {
                private val list = listOf(
                    Permission(ACCESS_COARSE_LOCATION, (23)),
                    Permission(ACCESS_FINE_LOCATION, (23)),
                )
            }
        }
    }

    data class SpecialState(val items: List<SpecialPermission> = permissions)
}
