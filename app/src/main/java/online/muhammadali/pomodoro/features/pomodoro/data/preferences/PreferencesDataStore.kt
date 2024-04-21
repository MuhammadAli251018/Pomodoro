package online.muhammadali.pomodoro.features.pomodoro.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import online.muhammadali.pomodoro.features.pomodoro.di.ContextProvider
import online.muhammadali.pomodoro.features.pomodoro.domain.PomodoroPreferences
import online.muhammadali.pomodoro.features.pomodoro.domain.PreferencesStore
import kotlin.Exception

val Context.pomodoroPreferences by preferencesDataStore("pomodoro-preferences")

class PreferencesDataStore(
    private val contextProvider: ContextProvider
) : PreferencesStore {

    private val SESSIONS_UNTIL_LONG_BREAK = intPreferencesKey("sessions-until-long-break")
    private val GROUPS_OF_SESSIONS = intPreferencesKey("long-break-session")
    private val FOCUS_PERIOD = intPreferencesKey("long-break-session")
    private val BREAK_PERIOD = intPreferencesKey("long-break-session")
    private val LONG_BREAK_PERIOD = intPreferencesKey("long-break-session")

    override suspend fun savePreferences(newPreferences: PomodoroPreferences): Unit = contextProvider.runWithContext {
        pomodoroPreferences.edit { preferences ->
            preferences[SESSIONS_UNTIL_LONG_BREAK] = newPreferences.sessionsForLongBreak
            preferences[GROUPS_OF_SESSIONS] = newPreferences.groupsOfSessions
            preferences[FOCUS_PERIOD] = newPreferences.focusPeriod
            preferences[BREAK_PERIOD] = newPreferences.breakPeriod
            preferences[LONG_BREAK_PERIOD] = newPreferences.longBreakPeriod
        }
    }

    override suspend fun getPreferences(): Result<PomodoroPreferences> = contextProvider.runWithContext {
        return try {
            val pref = pomodoroPreferences.data.first()
            Result.success(PomodoroPreferences(
                sessionsForLongBreak = pref[SESSIONS_UNTIL_LONG_BREAK] ?: throw Exception("Todo"),
                groupsOfSessions = pref[GROUPS_OF_SESSIONS] ?: throw Exception("Todo"),
                focusPeriod = pref[FOCUS_PERIOD] ?: throw Exception("Todo"),
                breakPeriod = pref[BREAK_PERIOD] ?: throw Exception("Todo"),
                longBreakPeriod = pref[LONG_BREAK_PERIOD] ?: throw Exception("Todo")
            ))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}