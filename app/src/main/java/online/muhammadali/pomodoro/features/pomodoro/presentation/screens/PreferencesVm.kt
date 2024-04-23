package online.muhammadali.pomodoro.features.pomodoro.presentation.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import online.muhammadali.pomodoro.features.pomodoro.data.preferences.PreferencesDataStore
import online.muhammadali.pomodoro.features.pomodoro.di.contextProvider
import online.muhammadali.pomodoro.features.pomodoro.domain.PomodoroPreferences
import online.muhammadali.pomodoro.features.pomodoro.domain.PreferencesStore

val defaultPreferences = PomodoroPreferences(
    focusPeriod = 25,
    breakPeriod = 5,
    longBreakPeriod = 15,
    sessionsForLongBreak = 4,
    groupsOfSessions = 2
)

class PreferencesVm : ViewModel(), PreferencesViewModel {

    private val preferencesStore = PreferencesDataStore(contextProvider)
    override fun Context.getCurrentPreferences(): StateFlow<PomodoroPreferences> {
        val preferencesStateFlow = MutableStateFlow(defaultPreferences)

        viewModelScope.launch {
            preferencesStore.getPreferences().onSuccess {
                preferencesStateFlow.emit(
                    it
                )
            }
        }

        return preferencesStateFlow
    }

    override fun Context.saveNewPreferences(newPref: PomodoroPreferences): Flow<Result<Unit>> {
        return flow {
            emit(
                try {
                    Result.success(preferencesStore.savePreferences(newPref))
                }
                catch (e: Exception) {
                    Result.failure(e)
                }
            )
        }

    }
}