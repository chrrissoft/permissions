package com.chrrissoft.permissions.common.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.work.ExistingWorkPolicy.KEEP
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.chrrissoft.permissions.Permission
import com.chrrissoft.permissions.common.app.SpecialPermission
import com.chrrissoft.permissions.common.ui.ScreenEvent.LocationEvent.BackgroundEvent
import com.chrrissoft.permissions.common.ui.ScreenEvent.LocationEvent.ForegroundEvent
import com.chrrissoft.permissions.common.ui.ScreenState.LocationState
import com.chrrissoft.permissions.common.ui.ScreenState.LocationState.Background
import com.chrrissoft.permissions.common.ui.ScreenState.LocationState.Foreground
import com.chrrissoft.permissions.common.ui.ScreenState.NormalState
import com.chrrissoft.permissions.common.ui.ScreenState.OneTimeState
import com.chrrissoft.permissions.common.ui.ScreenState.Page
import com.chrrissoft.permissions.common.ui.ScreenState.SpecialState
import com.chrrissoft.permissions.common.usecases.interfaces.ResolvePermissionEnabledUseCase
import com.chrrissoft.permissions.common.usecases.interfaces.ResolvePermissionsEnabledUseCase
import com.chrrissoft.permissions.common.usecases.interfaces.ResolveSpecialPermissionsEnabledUseCase
import com.chrrissoft.permissions.di.WorkManagerModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.chrrissoft.permissions.common.ui.ScreenEvent.NormalEvent.OnChangePermission as OnChangePermissionRunTime
import com.chrrissoft.permissions.common.ui.ScreenEvent.OneTimeEvent.OnChangePermission as OnChangePermissionOneTime


@HiltViewModel
class ScreenViewModel @Inject constructor(
    private val workManager: WorkManager,
    @WorkManagerModule.AccessBackgroundLocationRequest
    private val backAccessLocationRequest: OneTimeWorkRequest,
    private val ResolvePermissionEnabledUseCase: ResolvePermissionEnabledUseCase,
    private val ResolvePermissionsEnabledUseCase: ResolvePermissionsEnabledUseCase,
    private val ResolveSpecialPermissionsEnabledUseCase: ResolveSpecialPermissionsEnabledUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ScreenState())
    val stateFlow = _state.asStateFlow()
    val state get() = stateFlow.value
    private val _page get() = state.page
    private val _normals get() = state.runTime
    private val _oneTime get() = state.oneTime
    private val _location get() = state.location
    private val _backgroundLocation get() = _location.background
    private val _foregroundLocation get() = _location.foreground
    private val _special get() = state.special

    private val handler = EventHandler()

    fun handleEvent(event: ScreenEvent) {
        event.resolve(handler)
    }

    inner class EventHandler {
        val runTime = NormalEventHandler()
        val oneTime = OneTimeEventHandler()
        val location = LocationEventHandler()
        val special = SpecialEventHandler()

        fun onEvent(event: ScreenEvent.OnChangePage) {
            debug(event.data)
            updateState(page = event.data)
        }

        inner class NormalEventHandler {
            fun onEvent(event: OnChangePermissionRunTime) {
                val items = _normals.items.map {
                    if (event.data.name == it.name) event.data
                    else it
                }
                updateNormalState(items = items)
            }

            fun onEnableAsk() {
                updateNormalState(ResolvePermissionsEnabledUseCase(_normals.items))
            }
        }

        inner class OneTimeEventHandler {
            fun onEvent(event: OnChangePermissionOneTime) {
                val items = _oneTime.items.map {
                    if (event.data.name == it.name) event.data
                    else it
                }
                updateOneTimeState(items = items)
            }

            fun onEnableAsk() {
                updateOneTimeState(ResolvePermissionsEnabledUseCase(_oneTime.items))
            }
        }

        inner class LocationEventHandler {
            val foregroundHandler = Foreground()

            val backgroundHandler = Background()

            inner class Background {
                fun onEvent(event: BackgroundEvent.OnChangePermission) {
                    updateBackgroundLocation(item = event.data)
                }

                fun onStartBackgroundLocation() {
                    val name = "backAccessLocationRequest"
                    workManager.enqueueUniqueWork(name, KEEP, backAccessLocationRequest)
                }
            }

            inner class Foreground {
                fun onEvent(event: ForegroundEvent.OnChangePermission) {
                    val items = _foregroundLocation.items.map {
                        if (it.name == event.data.name) event.data else it
                    }
                    updateForegroundLocation(items = items)
                }
            }

            fun onEnableAsk() {
                val back = ResolvePermissionEnabledUseCase(_backgroundLocation.item)
                val fore = ResolvePermissionsEnabledUseCase(_foregroundLocation.items)
                updateLocationState(
                    background = _backgroundLocation.copy(item = back),
                    foreground = _foregroundLocation.copy(items = fore)
                )
            }
        }

        inner class SpecialEventHandler {
            fun onEnableAsk() {
                updateSpecialsState(ResolveSpecialPermissionsEnabledUseCase(_special.items))
            }
        }
    }

    private fun updateNormalState(items: List<Permission> = _normals.items) {
        val copy = _normals.copy(items = items)
        updateState(runTime = copy)
    }

    private fun updateOneTimeState(items: List<Permission> = _oneTime.items) {
        val copy = _oneTime.copy(items = items)
        updateState(oneTime = copy)
    }

    private fun updateBackgroundLocation(item: Permission = _backgroundLocation.item) {
        val copy = _backgroundLocation.copy(item = item)
        updateLocationState(background = copy)
    }

    private fun updateForegroundLocation(items: List<Permission> = _foregroundLocation.items) {
        val copy = _foregroundLocation.copy(items = items)
        updateLocationState(foreground = copy)
    }

    private fun updateLocationState(
        background: Background = _backgroundLocation,
        foreground: Foreground = _foregroundLocation,
    ) {
        val copy = _location.copy(background = background, foreground = foreground)
        updateState(location = copy)
    }

    private fun updateSpecialsState(items: List<SpecialPermission> = _special.items) {
        val copy = _special.copy(items = items)
        updateState(special = copy)
    }


    private fun updateState(
        page: Page = _page,
        runTime: NormalState = _normals,
        oneTime: OneTimeState = _oneTime,
        location: LocationState = _location,
        special: SpecialState = _special,
    ) {
        _state.update {
            it.copy(
                page = page,
                runTime = runTime,
                oneTime = oneTime,
                location = location,
                special = special,
            )
        }
    }

    private companion object {
        private fun debug(message: Any?) {
            Log.d(TAG, "$message")
        }
        private const val TAG = "ScreenViewModel"
    }
}
