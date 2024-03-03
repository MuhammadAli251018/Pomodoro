package online.muhammadali.pomodoro.features.pomodoro.domain.entities

import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration

abstract class Counter<State: CounterState> {

    protected abstract var state: State
    abstract val counterSettings: CounterSettings
    abstract val currentTime: StateFlow<Duration>

    abstract suspend fun startCounter(onFinished: () -> Unit)
    /**Resets counter to it's state*/
    abstract fun resetCounter()

    fun updateState(update: (State) -> State) {
        state = update(state)
    }
}