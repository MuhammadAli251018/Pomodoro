package online.muhammadali.pomodoro.features.pomodoro.presentation.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import online.muhammadali.pomodoro.features.pomodoro.domain.PomodoroPreferences

interface PreferencesViewModel {

    /** Returns default preferences when can't read preferences*/
    fun Context.getCurrentPreferences(): StateFlow<PomodoroPreferences>
    fun Context.saveNewPreferences(newPref: PomodoroPreferences): Flow<Result<Unit>>
}

@Composable
fun PreferencesViewModel.getCurrentPreferences(): State<PomodoroPreferences>
        = LocalContext.current.getCurrentPreferences().collectAsStateWithLifecycle()

/*
//  todo
@Composable
fun PreferencesViewModel.saveNewPreferences(newPref: PomodoroPreferences): Result<Unit>
        = LocalContext.current.saveNewPreferences(newPref)
*/
