package online.muhammadali.pomodoro.features.pomodoro.domain

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

private const val TAG = "PomodoroCounterTAG"

class PomodoroCounter(
    initialState: PomodoroCounterState,
    override val counterSettings: CounterSettings,
) : Counter<PomodoroCounterState>() {

    private val _state = MutableStateFlow(initialState)
    override var state: StateFlow<PomodoroCounterState> = _state.asStateFlow()
    private val _currentTime = MutableStateFlow(state.value.session)
    override val currentTime: StateFlow<Duration> = _currentTime.asStateFlow()

    private val workScope = CoroutineScope(Dispatchers.Default)
    init {
        workScope.launch {
            state.collectLatest {
                // update current time when new session starts
                if (it.changeReason == PomodoroCounterState.StateChangeReason.DurationChange) {
                    Log.d(TAG, "session: $it")
                    _currentTime.emit(it.session)
                }
            }
        }
    }

    override fun startOrPauseCounter() {
        workScope.launch {
            _state.emit(state.value.pauseOrResumeCounter())
            while (state.value.onGoing){
                val newValue = _currentTime.value - counterSettings.durationPerUpdate

                if (newValue < 50.milliseconds) {
                    _state.emit(state.value.nextSession())
                } else {
                    delay(counterSettings.durationPerUpdate)
                    _currentTime.emit(newValue)
                }
            }
        }
    }

    override fun resetCounter() {
        workScope.launch {

        }
    }
}