package online.muhammadali.pomodoro.features.pomodoro.domain

import android.util.Log
import online.muhammadali.pomodoro.features.pomodoro.presentation.screens.perform
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

private const val TAG = "PomodoroCounterStateTAG"
sealed class PomodoroCounterState : CounterState {

    companion object {


        private fun defaultTransitionRule(previousState: PomodoroCounterState): PomodoroCounterState {
            val changeReason = StateChangeReason.DurationChange

            return when(previousState) {
                is BreakState -> FocusState(
                    25.minutes,
                    Companion::defaultTransitionRule,
                    maxSessions = previousState.maxSessions,
                    changeReason = changeReason,
                    completedSessions = previousState.completedSessions
                )
                is FocusState -> if (previousState.completedSessions + 1 < previousState.maxSessions - 1) {
                    BreakState(
                        5.minutes,
                        Companion::defaultTransitionRule,
                        maxSessions = previousState.maxSessions,
                        changeReason = changeReason,
                        completedSessions = previousState.completedSessions + 1
                    )
                }
                else {

                    LongBreak(
                        15.minutes,
                        transitionRule = Companion::defaultTransitionRule,
                        maxSessions = previousState.maxSessions,
                        changeReason = changeReason,
                        completedSessions = previousState.completedSessions + 1
                    )
                }
                is LongBreak -> {
                    Log.d(TAG, "long break completed: ${previousState.completedSessions}, default parameter: ${previousState.maxSessions - 1}")
                    FocusState(
                        25.minutes,
                        Companion::defaultTransitionRule,
                        maxSessions = previousState.maxSessions,
                        changeReason = changeReason
                    )
                }
            }
        }
        fun getDefaultState(): PomodoroCounterState = FocusState(
            session = 25.minutes,
            transitionRule = Companion::defaultTransitionRule,
            maxSessions = 2,
            changeReason = StateChangeReason.PauseOrResume,
            completedSessions = -1
        )
    }
    enum class StateChangeReason {
        PauseOrResume,
        DurationChange
    }

    abstract val transitionRule: PomodoroCounterState.() -> PomodoroCounterState
    abstract val maxSessions: Int
    abstract val changeReason: StateChangeReason
    abstract val completedSessions: Int

    fun nextSession(): PomodoroCounterState = this.onDurationChange {
        transitionRule()
//        Log.d(TAG, "completed: $completedSessions, max: $maxSessions")
//        Log.d(TAG, "previous Session input: $this, finishedAll: ${ completedSessions >= (maxSessions - 1)}, reason: ${StateChangeReason.DurationChange} | Result: $result")
//        result
    }

    abstract fun pauseOrResumeCounter(): PomodoroCounterState
    fun pauseOnly(): PomodoroCounterState = if (onGoing) pauseOrResumeCounter() else this

    data class FocusState(
        override val session: Duration,
        override val transitionRule: PomodoroCounterState.() -> PomodoroCounterState,
        override val onGoing: Boolean = false,
        override val maxSessions: Int,
        override val changeReason: StateChangeReason,
        override val completedSessions: Int = -1
    ) : PomodoroCounterState() {
        override fun pauseOrResumeCounter(): PomodoroCounterState = this.perform { copy(onGoing = !onGoing, changeReason = StateChangeReason.PauseOrResume) }
    }

    data class BreakState(
        override val session: Duration,
        override val transitionRule: PomodoroCounterState.() -> PomodoroCounterState,
        override val onGoing: Boolean = false,
        override val maxSessions: Int,
        override val changeReason: StateChangeReason,
        override val completedSessions: Int
    ) : PomodoroCounterState() {
        override fun pauseOrResumeCounter(): PomodoroCounterState = this.perform { copy(onGoing = !onGoing, changeReason = StateChangeReason.PauseOrResume) }
    }

    data class LongBreak(
        override val session: Duration,
        override val transitionRule: PomodoroCounterState.() -> PomodoroCounterState,
        override val onGoing: Boolean = false,
        override val maxSessions: Int,
        override val changeReason: StateChangeReason,
        override val completedSessions: Int = maxSessions - 1
    ) : PomodoroCounterState() {
        override fun pauseOrResumeCounter(): PomodoroCounterState = this.perform { copy(onGoing = !onGoing, changeReason = StateChangeReason.PauseOrResume) }
    }

}

private inline fun PomodoroCounterState.onDurationChange(operation: PomodoroCounterState.() -> PomodoroCounterState) = this.perform {
    this.pauseOnly().operation()
}