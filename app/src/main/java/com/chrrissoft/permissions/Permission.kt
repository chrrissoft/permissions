package com.chrrissoft.permissions

import android.os.Build.VERSION.SDK_INT
import java.util.Locale.getDefault

data class Permission(
    val permission: String,
    val supportedApi: Int,
    val enabled: Boolean = false,
    val checkToShow: Boolean = false,
    val disableOnKill: Boolean = false,
) {
    val isSupported = SDK_INT >= supportedApi
    val name = permission.takeLastWhile { "$it" != "." }.replace("_", " ")
        .lowercase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString() }
}
