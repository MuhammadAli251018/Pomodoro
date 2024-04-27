package online.muhammadali.pomodoro.features.pomodoro.data.preferences

import android.util.Log
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import online.muhammadali.pomodoro.common.util.onFailure
import online.muhammadali.pomodoro.common.util.onSuccess
import online.muhammadali.pomodoro.features.pomodoro.di.ApplicationContextProvider
import online.muhammadali.pomodoro.features.pomodoro.di.ContextProvider
import online.muhammadali.pomodoro.features.pomodoro.presentation.screens.defaultPreferences
import org.junit.Test

private const val TAG = "PreferencesDataStoreTestTag"
var log: String = ""
    set(value) {
        Log.d(TAG, value)
    }
class PreferencesDataStoreTest {
    private val preferences = defaultPreferences.copy(
        focusPeriod = 30
    )
    private lateinit var preferencesStore : PreferencesDataStore
    @Test
    fun success_when_saves_new_preferences() = runTest {

        preferencesStore = PreferencesDataStore(ContextProvider(ApplicationContextProvider.applicationContext))
        log = "crated preferences [1]"
        preferencesStore.savePreferences(preferences)
        log = "writing preferences [2]"
        val result = preferencesStore.getPreferences().first()
        log = "reading preferences [3]"
        result.onSuccess {
            log = "result preferences [4]"
            assert(this == preferences)
        }
        .onFailure {
            assert(false)
        }
    }
}