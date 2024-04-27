package online.muhammadali.pomodoro.features.pomodoro.data.preferences

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import online.muhammadali.pomodoro.common.util.Failure
import online.muhammadali.pomodoro.features.pomodoro.di.ContextProvider
import online.muhammadali.pomodoro.features.pomodoro.domain.PomodoroPreferences
import online.muhammadali.pomodoro.features.pomodoro.domain.PreferencesStore
import online.muhammadali.pomodoro.features.pomodoro.presentation.screens.defaultPreferences
import online.muhammadali.pomodoro.common.util.Result
import online.muhammadali.pomodoro.common.util.Success
import java.io.IOException
import kotlin.Exception


val Context.pomodoroPreferences by preferencesDataStore("pomodoro-preferences")
private const val TAG = "PreferencesDataStoreTag"
class PreferencesDataStore(
    private val contextProvider: ContextProvider
) : PreferencesStore {


    private val SESSIONS_UNTIL_LONG_BREAK = intPreferencesKey("sessions_until_long_break")
    private val GROUPS_OF_SESSIONS = intPreferencesKey("groups_of_sessions")
    private val FOCUS_PERIOD = intPreferencesKey("focus_period")
    private val BREAK_PERIOD = intPreferencesKey("break_period")
    private val LONG_BREAK_PERIOD = intPreferencesKey("long_break_period")

    override suspend fun savePreferences(newPreferences: PomodoroPreferences): Unit = contextProvider.runWithContext {
        pomodoroPreferences.edit { pref ->
            pref[SESSIONS_UNTIL_LONG_BREAK] = newPreferences.sessionsForLongBreak
            pref[GROUPS_OF_SESSIONS] = newPreferences.groupsOfSessions
            pref[FOCUS_PERIOD] = newPreferences.focusPeriod
            pref[BREAK_PERIOD] = newPreferences.breakPeriod
            pref[LONG_BREAK_PERIOD] = newPreferences.longBreakPeriod
            Log.d(TAG, "prefs: ${pref[SESSIONS_UNTIL_LONG_BREAK]}, ${pref[GROUPS_OF_SESSIONS]}, ${pref[FOCUS_PERIOD]}, ${pref[BREAK_PERIOD]}, ${pref[LONG_BREAK_PERIOD]}")
        }
    }

    override suspend fun getPreferences(): Flow<Result<PomodoroPreferences>> = contextProvider.runWithContext {
        channelFlow<Result<PomodoroPreferences>>{
            try {
                pomodoroPreferences.data.collectLatest { pref ->
                    Log.d(TAG, "prefs: ${pref[SESSIONS_UNTIL_LONG_BREAK]}, ${pref[GROUPS_OF_SESSIONS]}, ${pref[FOCUS_PERIOD]}, ${pref[BREAK_PERIOD]}, ${pref[LONG_BREAK_PERIOD]}")
                    send(
                        Success(
                            PomodoroPreferences(
                                sessionsForLongBreak = pref[SESSIONS_UNTIL_LONG_BREAK] ?: throw unwrittenPrefsException,
                                groupsOfSessions = pref[GROUPS_OF_SESSIONS] ?: throw unwrittenPrefsException,
                                focusPeriod = pref[FOCUS_PERIOD] ?: throw unwrittenPrefsException,
                                breakPeriod = pref[BREAK_PERIOD] ?: throw unwrittenPrefsException,
                                longBreakPeriod = pref[LONG_BREAK_PERIOD] ?: throw unwrittenPrefsException
                            )
                        )
                    )
                }
            }
            catch (e: UnwrittenPrefsException) {
                writeDefaultPref()
                Log.d(TAG, "UnwrittenPrefsException rewriting")
                getPreferences()
            }
            catch(e: IOException)  {
                send(Failure(e))
            }
        }
    }
    private class UnwrittenPrefsException(message: String) : Exception(message)

    private val unwrittenPrefsException = UnwrittenPrefsException("preferences is not found")
    private suspend fun writeDefaultPref() = savePreferences(defaultPreferences)

}