package online.muhammadali.pomodoro.features.pomodoro.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import online.muhammadali.pomodoro.common.presentation.components.VerticalSpace
import online.muhammadali.pomodoro.common.ui.theme.PomodoroTheme
import online.muhammadali.pomodoro.features.pomodoro.presentation.components.PomodoroCounter
import online.muhammadali.pomodoro.features.pomodoro.presentation.components.SessionsIndicator

@Composable
fun PomodoroScreen(
    backgroundColor: Color,
    time: String,
    counterCompletion: Float,
    sessionsCount: Int,
    lastCompletedSession: Int,
    title: String,
    onCounterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        VerticalSpace(height = 30.dp)

        PomodoroCounter(
            modifier = Modifier,
            size = 350.dp,
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
            lastCompleted = lastCompletedSession
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
            title = "Focus Time"
        ) {

        }
    }
}