package online.muhammadali.pomodoro.features.pomodoro.presentation.screens

import online.muhammadali.pomodoro.features.pomodoro.domain.PomodoroPreferences

interface PreferencesViewModel {
    fun getCurrentPreferences(): Result<PomodoroPreferences>
    fun saveNewPreferences(newPref: PomodoroPreferences): Result<Unit>
}