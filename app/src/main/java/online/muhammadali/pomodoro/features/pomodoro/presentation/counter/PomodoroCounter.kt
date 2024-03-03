package online.muhammadali.pomodoro.features.pomodoro.presentation.counter

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import online.muhammadali.pomodoro.features.pomodoro.domain.entities.Counter
import online.muhammadali.pomodoro.features.pomodoro.domain.entities.CounterSettings
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

const val TAG = "PomodoroCounterTAG"

class PomodoroCounter(
    initialState: PomodoroCounterState,
    override val counterSettings: CounterSettings,
) : Counter<PomodoroCounterState>() {

    override var state: PomodoroCounterState = initialState
        set(value) {
                field = value.pauseOnly()
        }

    private val _currentTime = MutableStateFlow(state.session)
    override val currentTime: StateFlow<Duration> = _currentTime.asStateFlow()



    override suspend fun startCounter(onFinished: () -> Unit) {
        if (state.onGoing)
            return

        CoroutineScope(Dispatchers.Default).launch {
            _currentTime.apply {
                val newValue = value - counterSettings.durationPerUpdate
                if (newValue < 50.milliseconds) {
                    onFinished()
                        state = state.nextSession()
                }
                else {
                    emit(newValue)
                    delay(counterSettings.durationPerUpdate)
                }
            }
        }
    }

    override fun resetCounter() {
        state = state
    }
}