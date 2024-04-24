package online.muhammadali.pomodoro.features.pomodoro.domain

import online.muhammadali.pomodoro.features.pomodoro.presentation.screens.perform
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

private const val TAG = "PomodoroCounterStateTAG"
sealed class PomodoroCounterState : CounterState {

    companion object {
        class DefaultTransitionRule(
            private val preferences: PomodoroPreferences
        ) : TransitionRule {
            private fun defaultTransitionRule(
                previousState: PomodoroCounterState
            ): PomodoroCounterState {
                val changeReason = StateChangeReason.DurationChange

                return when(previousState) {
                    is BreakState -> FocusState(
                        preferences.focusPeriod.minutes,
                        this,
                        maxSessions = previousState.maxSessions,
                        changeReason = changeReason,
                        completedSessions = previousState.completedSessions
                    )
                    is FocusState -> if (previousState.completedSessions + 1 < previousState.maxSessions - 1) {
                        BreakState(
                            5.minutes,
                            this,
                            maxSessions = previousState.maxSessions,
                            changeReason = changeReason,
                            completedSessions = previousState.completedSessions + 1
                        )
                    }
                    else {

                        LongBreak(
                            15.minutes,
                            transitionRule = this,
                            maxSessions = previousState.maxSessions,
                            changeReason = changeReason,
                            completedSessions = previousState.completedSessions + 1
                        )
                    }
                    is LongBreak -> {
                        FocusState(
                            25.minutes,
                            this,
                            maxSessions = previousState.maxSessions,
                            changeReason = changeReason
                        )
                    }
                }
            }
            override val getNext: PomodoroCounterState.() -> PomodoroCounterState = {
                defaultTransitionRule(this)
            }
        }


        fun getPomodoroState(preferences: PomodoroPreferences): PomodoroCounterState = FocusState(
            session = preferences.focusPeriod.minutes,
            transitionRule = DefaultTransitionRule(preferences),
            maxSessions = preferences.sessionsForLongBreak,
            changeReason = StateChangeReason.PauseOrResume,
            completedSessions = -1
        )
    }
    enum class StateChangeReason {
        PauseOrResume,
        DurationChange
    }

    interface TransitionRule {
        val getNext: PomodoroCounterState.() -> PomodoroCounterState
    }

    abstract val transitionRule: TransitionRule
    abstract val maxSessions: Int
    abstract val changeReason: StateChangeReason
    abstract val completedSessions: Int

    fun nextSession(): PomodoroCounterState = this.onDurationChange {
        transitionRule.perform {
            getNext()
        }
    }

    abstract fun pauseOrResumeCounter(): PomodoroCounterState
    fun pauseOnly(): PomodoroCounterState = if (onGoing) pauseOrResumeCounter() else this

    data class FocusState(
        override val session: Duration,
        override val transitionRule: TransitionRule,
        override val onGoing: Boolean = false,
        override val maxSessions: Int,
        override val changeReason: StateChangeReason,
        override val completedSessions: Int = -1
    ) : PomodoroCounterState() {
        override fun pauseOrResumeCounter(): PomodoroCounterState = this.perform { copy(onGoing = !onGoing, changeReason = StateChangeReason.PauseOrResume) }
    }

    data class BreakState(
        override val session: Duration,
        override val transitionRule: TransitionRule,
        override val onGoing: Boolean = false,
        override val maxSessions: Int,
        override val changeReason: StateChangeReason,
        override val completedSessions: Int
    ) : PomodoroCounterState() {
        override fun pauseOrResumeCounter(): PomodoroCounterState = this.perform { copy(onGoing = !onGoing, changeReason = StateChangeReason.PauseOrResume) }
    }

    data class LongBreak(
        override val session: Duration,
        override val transitionRule: TransitionRule,
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