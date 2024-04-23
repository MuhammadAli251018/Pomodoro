package online.muhammadali.pomodoro.features.pomodoro.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import online.muhammadali.pomodoro.features.pomodoro.di.ContextProvider
import online.muhammadali.pomodoro.features.pomodoro.domain.PomodoroPreferences
import online.muhammadali.pomodoro.features.pomodoro.domain.PreferencesStore
import online.muhammadali.pomodoro.features.pomodoro.presentation.screens.defaultPreferences
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
                sessionsForLongBreak = pref[SESSIONS_UNTIL_LONG_BREAK] ?: throw unwrittenPrefsException,
                groupsOfSessions = pref[GROUPS_OF_SESSIONS] ?: throw unwrittenPrefsException,
                focusPeriod = pref[FOCUS_PERIOD] ?: throw unwrittenPrefsException,
                breakPeriod = pref[BREAK_PERIOD] ?: throw unwrittenPrefsException,
                longBreakPeriod = pref[LONG_BREAK_PERIOD] ?: throw unwrittenPrefsException
            ))
        }
        catch (e: UnwrittenPrefsException) {
            writeDefaultPref()
            getPreferences()
        }
        catch(e: Exception)  {
            // todo implement better
            Result.failure(e)
        }
    }
    private class UnwrittenPrefsException(message: String) : Exception(message)

    private val unwrittenPrefsException = UnwrittenPrefsException("prefernces is not found")
    private suspend fun writeDefaultPref() = savePreferences(defaultPreferences)

}