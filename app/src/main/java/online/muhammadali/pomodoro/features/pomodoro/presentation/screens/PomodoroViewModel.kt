package online.muhammadali.pomodoro.features.pomodoro.presentation.screens

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import online.muhammadali.pomodoro.common.util.getOrThrow
import online.muhammadali.pomodoro.features.pomodoro.data.preferences.PreferencesDataStore
import online.muhammadali.pomodoro.features.pomodoro.di.contextProvider
import online.muhammadali.pomodoro.features.pomodoro.domain.CounterSettings
import online.muhammadali.pomodoro.features.pomodoro.domain.PomodoroCounter
import online.muhammadali.pomodoro.features.pomodoro.domain.PomodoroCounterState
import online.muhammadali.pomodoro.features.pomodoro.domain.PomodoroPreferences
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

private const val TAG = "PomodoroViewModelTAG"

class PomodoroViewModel : ViewModel(), PomodoroScreenSAManager {

    private val settingsStore = PreferencesDataStore(contextProvider)
    private var preferences: PomodoroPreferences = defaultPreferences
    init {
        viewModelScope.launch(Dispatchers.IO){
            settingsStore.getPreferences().collectLatest {
                preferences = it.getOrThrow()
            }

        }
    }
    private val counter = PomodoroCounter(
        counterSettings = CounterSettings(durationPerUpdate = 50.milliseconds),
        initialState = PomodoroCounterState.getPomodoroState(preferences)
    )

    private val _screenMode: MutableStateFlow<PomodoroScreenMode> = MutableStateFlow(PomodoroScreenMode.FocusMode)
    private val _counterState: MutableStateFlow<UiPomodoroCounterState> = MutableStateFlow(UiPomodoroCounterState(
        currentTime = counter.state.value.session.minutesAndSeconds(),
        completion = 1f,
        state = if(counter.state.value.onGoing) UiPomodoroCounterState.State.OnGoing else UiPomodoroCounterState.State.Paused
    ))

    private val _indicatorState : MutableStateFlow<IndicatorState> = MutableStateFlow(
        IndicatorState(
            sessionsCount = counter.state.value.maxSessions,
            lastCompletedSession = -1
        )
    )

    override val screenMode: StateFlow<PomodoroScreenMode> = _screenMode.asStateFlow()
    override val counterState: StateFlow<UiPomodoroCounterState> = _counterState.asStateFlow()
    override val indicatorState: StateFlow<IndicatorState> = _indicatorState.asStateFlow()

    private suspend fun onPomodoroStateChange(newState: PomodoroCounterState) {
        withContext(Dispatchers.Main) {
            if (newState.changeReason == PomodoroCounterState.StateChangeReason.DurationChange)
            {
                // update indicator
                _indicatorState.perform {
                    emit(
                        when (_screenMode.value) {
                            PomodoroScreenMode.FocusMode -> {
                                value.copy(lastCompletedSession =
                                    if (value.lastCompletedSession < value.sessionsCount) {
                                        value.lastCompletedSession + 1
                                    }
                                    else {
                                        value.lastCompletedSession
                                    }
                                )
                            }
                            PomodoroScreenMode.ShortBreakMode -> value
                            PomodoroScreenMode.LongBreakMode -> value.copy(lastCompletedSession = -1)
                        }
                    )
                }
            }

            _screenMode.emit(newState.toPomodoroMode())
        }
    }

    init {
        viewModelScope.apply {
            launch(Dispatchers.Default) {
                counter.currentTime.collectLatest {
                    withContext(Dispatchers.Main) {
                        _counterState.emit(
                            counterState.value.copy(
                                currentTime = it.minutesAndSeconds(),
                                completion = (it / counter.state.value.session).toFloat()
                            )
                        )
                    }

                }
            }

            launch {
                counter.state.collectLatest { newState ->
                    onPomodoroStateChange(newState)
                }
            }
        }
    }

    override val onCounterClick: () -> Unit = {
            counter.startOrPauseCounter()
    }

}

private fun PomodoroCounterState.toPomodoroMode(): PomodoroScreenMode {
    return when(this) {
        is PomodoroCounterState.FocusState -> PomodoroScreenMode.FocusMode
        is PomodoroCounterState.LongBreak -> PomodoroScreenMode.LongBreakMode
        is PomodoroCounterState.BreakState -> PomodoroScreenMode.ShortBreakMode
    }
}

inline fun <I,O> I.perform(task: I.() -> O): O = task(this)

private fun Duration.minutesAndSeconds(): String {
    val minutes = (inWholeMinutes).perform { if (this < 10) "0$this" else this }
    val seconds = (inWholeSeconds - inWholeMinutes * 60).perform { if (this < 10) "0$this" else this }
    return "$minutes:$seconds"
}