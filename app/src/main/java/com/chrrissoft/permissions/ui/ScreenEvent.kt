package com.chrrissoft.permissions.ui

import com.chrrissoft.permissions.ui.ScreenState.PermissionItem
import com.chrrissoft.permissions.ui.ScreenState.SpecialState.SpecialPermissionItem
import com.chrrissoft.permissions.ui.ScreenViewModel.EventHandler.LocationEventHandler
import com.chrrissoft.permissions.ui.ScreenViewModel.EventHandler.LocationEventHandler.Background
import com.chrrissoft.permissions.ui.ScreenViewModel.EventHandler.LocationEventHandler.Foreground

sealed interface ScreenEvent {
    fun resolve(handler: ScreenViewModel.EventHandler) {
        when (this) {
            is OnChangePage -> handler.onEvent(event = this)
            is RunTimeRequestEvent -> resolve(handler.runTime)
            is OneTimeRunTimeRequestEvent -> resolve(handler.oneTime)
            is LocationEvent -> resolve(handler.location)
            is SpecialEvent -> { }
        }
    }

    data class OnChangePage(val data: ScreenState.Page) : ScreenEvent

    sealed interface RunTimeRequestEvent : ScreenEvent {
        fun resolve(handler: ScreenViewModel.EventHandler.RunTimeRequestEventHandler) {
            when (this) {
                is OnChangePermissionItem -> handler.onEvent(event = this)
                is OnChangeCheckToShowByName -> handler.onEvent(event = this)
            }
        }

        data class OnChangePermissionItem(val data: PermissionItem) : RunTimeRequestEvent

        data class OnChangeCheckToShowByName(val name: String, val check: Boolean) :
            RunTimeRequestEvent
    }

    sealed interface OneTimeRunTimeRequestEvent : ScreenEvent {
        fun resolve(handler: ScreenViewModel.EventHandler.OneTimeRunTimeRequestEventHandler) {
            when (this) {
                is OnChangePermissionItem -> handler.onEvent(event = this)
                is OnChangeCheckToShowByName -> handler.onEvent(event = this)
            }
        }

        data class OnChangePermissionItem(val data: PermissionItem) : OneTimeRunTimeRequestEvent

        data class OnChangeCheckToShowByName(val name: String, val check: Boolean) :
            OneTimeRunTimeRequestEvent
    }

    sealed interface LocationEvent : ScreenEvent {
        fun resolve(handler: LocationEventHandler) {
            when (this) {
                is ForegroundEvent -> resolve(handler.foregroundHandler)
                is BackgroundEvent -> resolve(handler.backgroundHandler)
            }
        }

        sealed interface BackgroundEvent : LocationEvent {
            fun resolve(handler: Background) {
                when (this) {
                    is OnChangePermissionItem -> handler.onEvent(this)
                    OnStartBackgroundLocationWork -> handler.onStartBackgroundLocation()
                }
            }

            data class OnChangePermissionItem(val data: PermissionItem) :
                BackgroundEvent

            object OnStartBackgroundLocationWork : BackgroundEvent
        }

        sealed interface ForegroundEvent : LocationEvent {
            fun resolve(handler: Foreground) {
                when (this) {
                    is OnChangePermissionItem -> handler.onEvent(event = this)
                    is OnChangeCheckToShowByName -> handler.onEvent(event = this)
                }
            }

            data class OnChangePermissionItem(val data: PermissionItem) :
                ForegroundEvent

            data class OnChangeCheckToShowByName(val name: String, val check: Boolean) :
                ForegroundEvent
        }
    }

    sealed interface SpecialEvent : ScreenEvent
}
