package online.muhammadali.pomodoro.features.pomodoro.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import online.muhammadali.pomodoro.R
import online.muhammadali.pomodoro.common.presentation.components.VerticalSpace
import online.muhammadali.pomodoro.common.ui.theme.PomodoroTheme
import online.muhammadali.pomodoro.features.pomodoro.domain.PomodoroPreferences
import online.muhammadali.pomodoro.features.pomodoro.presentation.components.NumberCounter
import online.muhammadali.pomodoro.features.pomodoro.presentation.screens.navigation.Screen


@Composable
fun PreferencesScreen (
    viewModel: PreferencesViewModel,
    navHostController: NavController
) {
    val preferences by viewModel.getCurrentPreferences().collectAsStateWithLifecycle()
    PreferencesScreen(
        preferences = preferences,
        onFocusPeriodChange = {
            viewModel.saveNewPreferences(
                preferences.copy(
                    focusPeriod = it
                )
            )
        },
        onBreakPeriodChange = {
            viewModel.saveNewPreferences(
                preferences.copy(
                    breakPeriod = it
                )
            )
        },
        onLongBreakPeriodChange = {
            viewModel.saveNewPreferences(
                preferences.copy(
                    longBreakPeriod = it
                )
            )
        },
        onSessionToLongBreakChange = {
            viewModel.saveNewPreferences(
                preferences.copy(
                    sessionsForLongBreak = it
                )
            )
        },
        onSessionsGroupsChange = {
            viewModel.saveNewPreferences(
                    preferences.copy(
                        groupsOfSessions = it
                    )
                )
        },
        onToPomodoroCounter = {
            navHostController.navigate(Screen.Pomodoro.route)
        }
    )
}
/***
 * @param preferences is initial value for the state*/
@Composable
fun PreferencesScreen (
    preferences: PomodoroPreferences,
    onFocusPeriodChange: (Int) -> Unit,
    onBreakPeriodChange: (Int) -> Unit,
    onLongBreakPeriodChange: (Int) -> Unit,
    onSessionToLongBreakChange: (Int) -> Unit,
    onSessionsGroupsChange: (Int) -> Unit,
    onToPomodoroCounter: () -> Unit
) {
    Column (
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ){
        VerticalSpace(height = 20.dp)

        Icon(
            modifier = Modifier
                .size(40.dp)
                .clickable(onClick = onToPomodoroCounter),
            painter = painterResource(id = R.drawable.left_arrow_ic),
            contentDescription = "Back to pomodoro",
            tint = Color.White
        )

        VerticalSpace(height = 20.dp)

        //  focus period
        NumberCounter(
            modifier = Modifier,
            min = 10,
            max = 45,
            current = preferences.focusPeriod,
            step = 1,
            title = "Focus Period",
            backgroundColor = MaterialTheme.colorScheme.background,
            titleFontSize = 30.sp,
            numberFontSize = 45.sp,
            onChange = onFocusPeriodChange
        )

        VerticalSpace(height = 10.dp)

        //  break period
        NumberCounter(
            modifier = Modifier,
            min = 1,
            max = 10,
            current = preferences.breakPeriod,
            step = 1,
            title = "Break Period",
            backgroundColor = MaterialTheme.colorScheme.background,
            titleFontSize = 30.sp,
            numberFontSize = 45.sp,
            onChange = onBreakPeriodChange
        )

        VerticalSpace(height = 10.dp)

        //  long break period
        NumberCounter(
            modifier = Modifier,
            min = 5,
            max = 20,
            current = preferences.longBreakPeriod,
            step = 1,
            title = "Long Break Period",
            backgroundColor = MaterialTheme.colorScheme.background,
            titleFontSize = 30.sp,
            numberFontSize = 45.sp,
            onChange = onLongBreakPeriodChange
        )

        VerticalSpace(height = 10.dp)

        //  sessions for long break
        NumberCounter(
            modifier = Modifier,
            min = 1,
            max = 8,
            current = preferences.sessionsForLongBreak,
            step = 1,
            title = "Sessions until long break",
            backgroundColor = MaterialTheme.colorScheme.background,
            titleFontSize = 25.sp,
            numberFontSize = 45.sp,
            onChange = onSessionToLongBreakChange
        )

        VerticalSpace(height = 10.dp)

        //  sessions groups period
        NumberCounter(
            modifier = Modifier,
            min = 1,
            max = 4,
            current = preferences.groupsOfSessions,
            step = 1,
            title = "Sessions Groups",
            backgroundColor = MaterialTheme.colorScheme.background,
            titleFontSize = 30.sp,
            numberFontSize = 45.sp,
            onChange = onSessionsGroupsChange
        )

        VerticalSpace(height = 10.dp)
    }
}


@Preview
@Composable
fun PreferencesScreenPreview() {
    PomodoroTheme {
        PreferencesScreen(
            preferences = PomodoroPreferences(
                focusPeriod = 25,
                breakPeriod = 5,
                longBreakPeriod = 15,
                sessionsForLongBreak = 4,
                groupsOfSessions = 4
            ),
            onFocusPeriodChange = {},
            onBreakPeriodChange = {},
            onLongBreakPeriodChange = {},
            onSessionToLongBreakChange = {},
            {}
        ) {

        }
    }
}