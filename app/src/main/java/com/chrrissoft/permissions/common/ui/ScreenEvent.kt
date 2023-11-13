package com.chrrissoft.permissions.common.ui

import com.chrrissoft.permissions.Permission
import com.chrrissoft.permissions.common.ui.ScreenViewModel.EventHandler
import com.chrrissoft.permissions.common.ui.ScreenViewModel.EventHandler.LocationEventHandler
import com.chrrissoft.permissions.common.ui.ScreenViewModel.EventHandler.LocationEventHandler.Background
import com.chrrissoft.permissions.common.ui.ScreenViewModel.EventHandler.LocationEventHandler.Foreground
import com.chrrissoft.permissions.common.ui.ScreenViewModel.EventHandler.NormalEventHandler
import com.chrrissoft.permissions.common.ui.ScreenViewModel.EventHandler.OneTimeEventHandler
import com.chrrissoft.permissions.common.ui.ScreenViewModel.EventHandler.SpecialEventHandler

sealed interface ScreenEvent {
    fun resolve(handler: EventHandler) {
        when (this) {
            is OnChangePage -> handler.onEvent(event = this)
            is NormalEvent -> resolve(handler.runTime)
            is OneTimeEvent -> resolve(handler.oneTime)
            is LocationEvent -> resolve(handler.location)
            is SpecialEvent -> resolve(handler.special)
        }
    }

    data class OnChangePage(val data: ScreenState.Page) : ScreenEvent

    sealed interface NormalEvent : ScreenEvent {
        fun resolve(handler: NormalEventHandler) {
            when (this) {
                is OnEnableAsk -> handler.onEnableAsk()
                is OnChangePermission -> handler.onEvent(event = this)
            }
        }

        data class OnChangePermission(val data: Permission) : NormalEvent

        object OnEnableAsk : NormalEvent
    }

    sealed interface OneTimeEvent : ScreenEvent {
        fun resolve(handler: OneTimeEventHandler) {
            when (this) {
                is OnEnableAsk -> handler.onEnableAsk()
                is OnChangePermission -> handler.onEvent(event = this)
            }
        }

        data class OnChangePermission(val data: Permission) : OneTimeEvent

        object OnEnableAsk : OneTimeEvent
    }

    sealed interface LocationEvent : ScreenEvent {
        fun resolve(handler: LocationEventHandler) {
            when (this) {
                is ForegroundEvent -> resolve(handler.foregroundHandler)
                is BackgroundEvent -> resolve(handler.backgroundHandler)
                OnEnableAsk -> handler.onEnableAsk()
            }
        }

        sealed interface BackgroundEvent : LocationEvent {
            fun resolve(handler: Background) {
                when (this) {
                    is OnChangePermission -> handler.onEvent(this)
                    OnStartBackgroundLocationWork -> handler.onStartBackgroundLocation()
                }
            }

            data class OnChangePermission(val data: Permission) : BackgroundEvent

            object OnStartBackgroundLocationWork : BackgroundEvent
        }

        sealed interface ForegroundEvent : LocationEvent {
            fun resolve(handler: Foreground) {
                when (this) {
                    is OnChangePermission -> handler.onEvent(event = this)
                }
            }

            data class OnChangePermission(val data: Permission) : ForegroundEvent
        }

        object OnEnableAsk : LocationEvent
    }

    sealed interface SpecialEvent : ScreenEvent {
        fun resolve(handler: SpecialEventHandler) {
            when (this) {
                is OnEnableAsk -> handler.onEnableAsk()
            }
        }
        object OnEnableAsk : SpecialEvent
    }
}
