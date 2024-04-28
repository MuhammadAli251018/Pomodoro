package online.muhammadali.pomodoro.features.pomodoro.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import online.muhammadali.pomodoro.common.util.extractData
import online.muhammadali.pomodoro.features.pomodoro.data.preferences.PreferencesDataStore
import online.muhammadali.pomodoro.features.pomodoro.di.contextProvider
import online.muhammadali.pomodoro.features.pomodoro.domain.PomodoroPreferences

val defaultPreferences = PomodoroPreferences(
    focusPeriod = 25,
    breakPeriod = 5,
    longBreakPeriod = 15,
    sessionsForLongBreak = 4,
    groupsOfSessions = 2
)

private const val TAG = "PreferencesVmTag"
class PreferencesVm : ViewModel(), PreferencesViewModel {

    private val preferencesStore = PreferencesDataStore(contextProvider)
    override fun getCurrentPreferences(): StateFlow<PomodoroPreferences> {

        val prefFlow = MutableStateFlow(defaultPreferences)
        viewModelScope.launch{
            prefFlow.apply {
                preferencesStore.getPreferences().extractData(flowCollector = this) {
                    // todo handle
                    //Log.d(TAG, "error while reading preferences: ${cause}, ${message}")
                }
            }
        }

        return prefFlow.asStateFlow()
    }

    override fun saveNewPreferences(newPref: PomodoroPreferences) {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesStore.savePreferences(newPref)
        }
    }
}