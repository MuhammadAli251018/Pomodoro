package online.muhammadali.pomodoro.features.pomodoro.domain

data class PomodoroSettings(
    val sessionsForLongBreak: Int,
    val groupsOfSessions: Int,
    val focusPeriod: Int,
    val breakPeriod: Int,
    val longBreakPeriod: Int
)