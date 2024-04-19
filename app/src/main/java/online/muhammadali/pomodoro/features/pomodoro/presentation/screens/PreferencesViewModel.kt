package online.muhammadali.pomodoro.features.pomodoro.presentation.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import online.muhammadali.pomodoro.features.pomodoro.domain.PomodoroPreferences

interface PreferencesViewModel {

    /** Returns default preferences when can't read preferences*/
    fun Context.getCurrentPreferences(): PomodoroPreferences
    fun Context.saveNewPreferences(newPref: PomodoroPreferences): Result<Unit>
}

@Composable
fun PreferencesViewModel.getCurrentPreferences(): PomodoroPreferences
        = LocalContext.current.getCurrentPreferences()

@Composable
fun PreferencesViewModel.saveNewPreferences(newPref: PomodoroPreferences): Result<Unit>
        = LocalContext.current.saveNewPreferences(newPref)
