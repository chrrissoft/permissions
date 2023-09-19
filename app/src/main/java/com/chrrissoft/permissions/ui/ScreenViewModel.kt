package com.chrrissoft.permissions.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.work.ExistingWorkPolicy.KEEP
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.chrrissoft.permissions.WorkManagerModule
import com.chrrissoft.permissions.ui.ScreenEvent.LocationEvent.BackgroundEvent
import com.chrrissoft.permissions.ui.ScreenEvent.LocationEvent.ForegroundEvent
import com.chrrissoft.permissions.ui.ScreenEvent.OneTimeRunTimeRequestEvent.*
import com.chrrissoft.permissions.ui.ScreenState.*
import com.chrrissoft.permissions.ui.ScreenState.SpecialState.SpecialPermissionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.chrrissoft.permissions.ui.ScreenEvent.OneTimeRunTimeRequestEvent.OnChangeCheckToShowByName as OnChangeCheckToShowByNameOneTime
import com.chrrissoft.permissions.ui.ScreenEvent.OneTimeRunTimeRequestEvent.OnChangePermissionItem as OnChangePermissionItemOneTime
import com.chrrissoft.permissions.ui.ScreenEvent.RunTimeRequestEvent.OnChangeCheckToShowByName as OnChangeCheckToShowByNameRunTime
import com.chrrissoft.permissions.ui.ScreenEvent.RunTimeRequestEvent.OnChangePermissionItem as OnChangePermissionItemRunTime


@HiltViewModel
class ScreenViewModel @Inject constructor(
    private val workManager: WorkManager,
    @WorkManagerModule.AccessBackgroundLocationRequest
    private val backAccessLocationRequest: OneTimeWorkRequest,
) : ViewModel() {
    private val _state = MutableStateFlow(ScreenState())
    val stateFlow = _state.asStateFlow()
    val state get() = stateFlow.value
    private val _page get() = state.page
    private val _runTime get() = state.runTime
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
        val runTime = RunTimeRequestEventHandler()
        val oneTime = OneTimeRunTimeRequestEventHandler()
        val location = LocationEventHandler()

        fun onEvent(event: ScreenEvent.OnChangePage) {
            debug(event.data)
            updateState(page = event.data)
        }

        inner class RunTimeRequestEventHandler {
            fun onEvent(event: OnChangePermissionItemRunTime) {
                val items = _runTime.items.map {
                    if (event.data.name == it.name) event.data
                    else it
                }
                updateRunTimeState(items = items)
            }

            fun onEvent(event: OnChangeCheckToShowByNameRunTime) {
                val items = _runTime.items.map {
                    if (it.name == event.name) it.copy(checkToShow = event.check)
                    else it
                }
                updateRunTimeState(items = items)
            }
        }

        inner class OneTimeRunTimeRequestEventHandler {
            fun onEvent(event: OnChangePermissionItemOneTime) {
                val items = _oneTime.items.map {
                    if (event.data.name == it.name) event.data
                    else it
                }
                updateOneTimeState(items = items)
            }

            fun onEvent(event: OnChangeCheckToShowByNameOneTime) {
                val items = _oneTime.items.map {
                    if (it.name == event.name) it.copy(checkToShow = event.check)
                    else it
                }
                updateOneTimeState(items = items)
            }
        }

        inner class LocationEventHandler {
            val foregroundHandler = Foreground()

            val backgroundHandler = Background()

            inner class Background {
                fun onEvent(event: BackgroundEvent.OnChangePermissionItem) {
                    updateBackgroundLocation(item = event.data)
                }

                fun onStartBackgroundLocation() {
                    val name = "backAccessLocationRequest"
                    workManager.enqueueUniqueWork(name, KEEP, backAccessLocationRequest)
                }
            }

            inner class Foreground {
                fun onEvent(event: ForegroundEvent.OnChangePermissionItem) {
                    val items = _foregroundLocation.items.map {
                        if (it.name == event.data.name) event.data else it
                    }
                    updateForegroundLocation(items = items)
                }

                fun onEvent(event: ForegroundEvent.OnChangeCheckToShowByName) {
                    val items = _foregroundLocation.items.map {
                        if (it.name == event.name) it.copy(checkToShow = event.check)
                        else it
                    }
                    updateForegroundLocation(items = items)
                }
            }
        }
    }

    private fun updateRunTimeState(items: List<PermissionItem> = _runTime.items) {
        val copy = _runTime.copy(items = items)
        updateState(runTime = copy)
    }

    private fun updateOneTimeState(items: List<PermissionItem> = _oneTime.items) {
        val copy = _oneTime.copy(items = items)
        updateState(oneTime = copy)
    }

    private fun updateBackgroundLocation(item: PermissionItem = _backgroundLocation.item) {
        val copy = _backgroundLocation.copy(item = item)
        updateLocationState(background = copy)
    }

    private fun updateForegroundLocation(items: List<PermissionItem> = _foregroundLocation.items) {
        val copy = _foregroundLocation.copy(items = items)
        updateLocationState(foreground = copy)
    }

    private fun updateLocationState(
        background: LocationState.Background = _backgroundLocation,
        foreground: LocationState.Foreground = _foregroundLocation,
    ) {
        val copy = _location.copy(background = background, foreground = foreground)
        updateState(location = copy)
    }

    fun updateSpecialsState(items: List<SpecialPermissionItem> = _special.items, foo: Any?) {
        val copy = _special.copy(items = items, foo = foo)
        updateState(special = copy)
    }

    private fun updateState(
        page: Page = _page,
        runTime: RunTimeRequestState = _runTime,
        oneTime: OneTimeRequestState = _oneTime,
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
