package online.muhammadali.pomodoro.features.pomodoro.presentation.screens

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import online.muhammadali.pomodoro.features.pomodoro.domain.PomodoroPreferences

interface PreferencesViewModel {

    /** Returns default preferences when can't read preferences*/
    fun getCurrentPreferences(): StateFlow<PomodoroPreferences>
    fun saveNewPreferences(newPref: PomodoroPreferences): Flow<Result<Unit>>
}