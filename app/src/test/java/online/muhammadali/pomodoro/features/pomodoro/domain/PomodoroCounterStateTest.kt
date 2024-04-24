package online.muhammadali.pomodoro.features.pomodoro.domain

import online.muhammadali.pomodoro.features.pomodoro.presentation.screens.defaultPreferences
import org.junit.Test
import kotlin.time.DurationUnit

infix fun <T> T.equals (other: T): Boolean {
    return this == other
}

class PomodoroCounterStateTest {

    // test the state from getState function
    @Test
    fun `success when returns state from preferences`() {
        PomodoroCounterState.getPomodoroState(
            defaultPreferences
        ).apply {
            assert(
                this is PomodoroCounterState.FocusState &&
                completedSessions == -1 &&
                session.toInt(DurationUnit.MINUTES) == defaultPreferences.focusPeriod &&
                maxSessions == defaultPreferences.sessionsForLongBreak &&
                changeReason == PomodoroCounterState.StateChangeReason.PauseOrResume
            )
        }
    }

    @Test
    fun `success when transition rule returns break period`() {
        PomodoroCounterState.getPomodoroState(
            defaultPreferences
        ).nextSession().apply {

            assert(
                session.inWholeMinutes.toInt() equals defaultPreferences.breakPeriod
                        and (this is PomodoroCounterState.BreakState)
                        and (changeReason equals PomodoroCounterState.StateChangeReason.DurationChange)
                        and (maxSessions equals defaultPreferences.sessionsForLongBreak)
                        and (completedSessions equals 0)

            )
        }
    }

    @Test
    fun `success when transition rule returns focus period`() {
        PomodoroCounterState.getPomodoroState(
            defaultPreferences
        ).nextSession().nextSession().apply {

            listOf(
                session.inWholeMinutes.toInt() equals defaultPreferences.focusPeriod,
                (this is PomodoroCounterState.FocusState),
                (changeReason equals PomodoroCounterState.StateChangeReason.DurationChange),
                (maxSessions equals defaultPreferences.sessionsForLongBreak),
                (completedSessions equals 1)

            ).forEachIndexed { index, result ->
                println("result[$index] -> $result")
            }
            assert(
                session.inWholeMinutes.toInt() equals defaultPreferences.focusPeriod
                        and (this is PomodoroCounterState.FocusState)
                        and (changeReason equals PomodoroCounterState.StateChangeReason.DurationChange)
                        and (maxSessions equals defaultPreferences.sessionsForLongBreak)
                        and (completedSessions equals 0)

            )
        }
    }

    @Test
    fun `success when transition rule returns long break period`() {
        PomodoroCounterState.getPomodoroState(
            defaultPreferences.copy(
                sessionsForLongBreak = 2
            )
        ).nextSession().nextSession().nextSession().apply {
            assert(
                session.inWholeMinutes.toInt() equals defaultPreferences.longBreakPeriod
                        and (this is PomodoroCounterState.LongBreak)
                        and (changeReason equals PomodoroCounterState.StateChangeReason.DurationChange)
                        and (maxSessions equals 2)
                        and (completedSessions equals 1)

            )
        }
    }

    @Test
    fun `success when starts a new group`() {
        PomodoroCounterState.getPomodoroState(
            defaultPreferences.copy(
                sessionsForLongBreak = 2
            )
        ).nextSession().nextSession().nextSession().nextSession().apply {
            assert(
                session.inWholeMinutes.toInt() equals defaultPreferences.focusPeriod
                        and (this is PomodoroCounterState.FocusState)
                        and (changeReason equals PomodoroCounterState.StateChangeReason.DurationChange)
                        and (maxSessions equals 2)
                        and (completedSessions equals -1)

            )
        }
    }

    /* Todo fix bug the sessions count for long break is longer with one
        the bug exists in the transition rule
     */

    @Test
    fun `success when starts returns long break(single focus session)`() {
        PomodoroCounterState.getPomodoroState(
            defaultPreferences.copy(
                sessionsForLongBreak = 1
            )
        ).nextSession().nextSession().nextSession().nextSession().apply {
            assert(
                session.inWholeMinutes.toInt() equals defaultPreferences.focusPeriod
                        and (this is PomodoroCounterState.FocusState)
                        and (changeReason equals PomodoroCounterState.StateChangeReason.DurationChange)
                        and (maxSessions equals 2)
                        and (completedSessions equals -1)

            )
        }
    }

}