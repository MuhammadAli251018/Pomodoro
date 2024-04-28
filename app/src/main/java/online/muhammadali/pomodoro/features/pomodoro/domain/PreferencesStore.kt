package online.muhammadali.pomodoro.features.pomodoro.domain

import kotlinx.coroutines.flow.Flow
import online.muhammadali.pomodoro.common.util.Result

interface PreferencesStore {
    suspend fun savePreferences(newPreferences: PomodoroPreferences)

    suspend fun getPreferences(): Flow<Result<PomodoroPreferences>>
}