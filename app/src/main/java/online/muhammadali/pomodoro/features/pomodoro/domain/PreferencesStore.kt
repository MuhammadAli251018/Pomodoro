package online.muhammadali.pomodoro.features.pomodoro.domain

interface PreferencesStore {
    suspend fun savePreferences(newPreferences: PomodoroPreferences)

    suspend fun getPreferences(): Result<PomodoroPreferences>
}