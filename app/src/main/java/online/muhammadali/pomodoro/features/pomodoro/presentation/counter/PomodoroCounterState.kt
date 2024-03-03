package online.muhammadali.pomodoro.features.pomodoro.presentation.counter

import online.muhammadali.pomodoro.features.pomodoro.domain.entities.CounterState
import online.muhammadali.pomodoro.features.pomodoro.presentation.screens.perform
import kotlin.time.Duration

sealed class PomodoroCounterState :  CounterState {
    abstract val transitionRule: (PomodoroCounterState) -> PomodoroCounterState

    fun nextSession(): PomodoroCounterState = this.perform {
        pauseOnly()
        transitionRule(this)
    }
    abstract fun pauseOrResumeCounter(): PomodoroCounterState
    fun pauseOnly(): PomodoroCounterState = if (onGoing) pauseOrResumeCounter() else this

    data class FocusState(
        override val session: Duration,
        override val transitionRule: (PomodoroCounterState) -> PomodoroCounterState,
        override val onGoing: Boolean = false
    ) : PomodoroCounterState() {
        override fun pauseOrResumeCounter(): PomodoroCounterState = this.perform { copy(onGoing = !onGoing) }
    }

    data class BreakState(
        override val session: Duration,
        override val transitionRule: (PomodoroCounterState) -> PomodoroCounterState,
        override val onGoing: Boolean = false
    ) : PomodoroCounterState() {
        override fun pauseOrResumeCounter(): PomodoroCounterState = this.perform { copy(onGoing = !onGoing) }
    }

    data class LongBreak(
        override val session: Duration,
        override val transitionRule: (PomodoroCounterState) -> PomodoroCounterState,
        override val onGoing: Boolean = false
    ) : PomodoroCounterState() {
        override fun pauseOrResumeCounter(): PomodoroCounterState = this.perform { copy(onGoing = !onGoing) }
    }

}

