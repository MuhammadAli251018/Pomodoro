package online.muhammadali.pomodoro.features.pomodoro.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow
import online.muhammadali.pomodoro.common.presentation.components.VerticalSpace
import online.muhammadali.pomodoro.common.ui.theme.PomodoroTheme
import online.muhammadali.pomodoro.features.pomodoro.presentation.components.PomodoroCounter
import online.muhammadali.pomodoro.features.pomodoro.presentation.components.SessionsIndicator
import online.muhammadali.pomodoro.features.pomodoro.presentation.components.SettingsButton

private const val TAG  = "PomodoroScreenTAG"

sealed class PomodoroScreenMode {
    @get:Composable
    abstract val backgroundColor: Color
    abstract val title: String

    data object FocusMode : PomodoroScreenMode() {
        override val backgroundColor: Color
            @Composable
            get() {
               return MaterialTheme.colorScheme.secondary
            }

        override val title: String = "Focus Time"
    }

    data object LongBreakMode : PomodoroScreenMode() {
        override val backgroundColor: Color
            @Composable
            get() {
                return MaterialTheme.colorScheme.tertiary
            }

        override val title: String = "Long Break Time"
    }

    data object ShortBreakMode : PomodoroScreenMode() {
        override val backgroundColor: Color
            @Composable
            get() {
                return MaterialTheme.colorScheme.background
            }

        override val title: String = "Break Time"
    }
}

data class IndicatorState(
    val sessionsCount: Int,
    val lastCompletedSession: Int
)

data class UiPomodoroCounterState (
    val currentTime: String,
    val completion: Float,
    val state: State
) {
    sealed class State {

        abstract fun reverse(): State
        data object OnGoing : State() {
            override fun reverse(): State {
                return Paused
            }
        }

        data object Paused : State() {
            override fun reverse(): State {
                return OnGoing
            }
        }
    }
}

interface PomodoroScreenSAManager {
    val screenMode: StateFlow<PomodoroScreenMode>
    val counterState: StateFlow<UiPomodoroCounterState>
    val indicatorState: StateFlow<IndicatorState>
    val onCounterClick: () -> Unit
}

@Composable
fun PomodoroScreen(
    stateActionManager: PomodoroScreenSAManager
) {
    val screenMode by stateActionManager.screenMode.collectAsStateWithLifecycle()
    val counterState by stateActionManager.counterState.collectAsStateWithLifecycle()
    val indicatorState by stateActionManager.indicatorState.collectAsStateWithLifecycle()

    PomodoroScreen(
        screenMode = screenMode,
        counterState = counterState,
        indicatorState = indicatorState,
        onCounterClick = stateActionManager.onCounterClick
    )
}
@Composable
fun PomodoroScreen(
    screenMode: PomodoroScreenMode,
    counterState: UiPomodoroCounterState,
    indicatorState: IndicatorState,
    onCounterClick: () -> Unit
) {
    Log.d(TAG, "counterState: ${counterState.currentTime}")
    PomodoroScreen(
        backgroundColor = screenMode.backgroundColor,
        time = counterState.currentTime,
        counterCompletion = counterState.completion,
        sessionsCount = indicatorState.sessionsCount,
        lastCompletedSession = indicatorState.lastCompletedSession,
        title = screenMode.title,
        onCounterClick = onCounterClick,
        onToPreferences = {
            //Todo
        }
    )
}

// stateless
@Composable
private fun PomodoroScreen(
    backgroundColor: Color,
    time: String,
    counterCompletion: Float,
    sessionsCount: Int,
    lastCompletedSession: Int,
    title: String,
    onToPreferences: () -> Unit,
    onCounterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        VerticalSpace(height = 30.dp)

        //  to settings
        SettingsButton(
            modifier = Modifier
                .padding(horizontal = 25.dp)
                .align(Alignment.End),
            iconSize = DpSize(width = 35.dp, height = 35.dp),
            onClick = onToPreferences
        )

        VerticalSpace(height = 30.dp)

        PomodoroCounter(
            modifier = Modifier,
            size = 300.dp,
            time = time,
            completion = counterCompletion,
            fontSize = 60.sp,
            fontWeight = FontWeight.Light,
            onClick = onCounterClick
        )

        VerticalSpace(height = 40.dp)

        SessionsIndicator(
            modifier = Modifier,
            sessionsCount = sessionsCount,
            lastCompleted = lastCompletedSession,
            diameterLength = 15.dp,
            spaceLength = 15.dp
        )

        VerticalSpace(height = 100.dp)

        Text(
            text = title,
            fontSize = 35.sp,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Preview
@Composable
fun PomodoroScreenPreview() {
    PomodoroTheme {
        PomodoroScreen(
            backgroundColor = MaterialTheme.colorScheme.secondary,
            time = "25:00",
            counterCompletion = 0.75f,
            sessionsCount = 4,
            lastCompletedSession = 1,
            title = "Focus Time",
            onToPreferences = {}
        ) {

        }
    }
}