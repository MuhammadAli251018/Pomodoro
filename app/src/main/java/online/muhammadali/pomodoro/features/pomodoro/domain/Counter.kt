package online.muhammadali.pomodoro.features.pomodoro.domain

import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration

abstract class Counter<State: CounterState> {

    abstract var state: StateFlow<State>
        protected set
    abstract val counterSettings: CounterSettings
    abstract val currentTime: StateFlow<Duration>

    abstract fun startOrPauseCounter()
    /**Resets counter to it's state*/
    abstract fun resetCounter()

}