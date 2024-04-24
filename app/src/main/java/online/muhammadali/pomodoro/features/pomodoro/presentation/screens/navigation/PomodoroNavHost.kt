package online.muhammadali.pomodoro.features.pomodoro.presentation.screens.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import online.muhammadali.pomodoro.features.pomodoro.presentation.screens.PomodoroScreen
import online.muhammadali.pomodoro.features.pomodoro.presentation.screens.PomodoroViewModel
import online.muhammadali.pomodoro.features.pomodoro.presentation.screens.PreferencesScreen
import online.muhammadali.pomodoro.features.pomodoro.presentation.screens.PreferencesVm

@Composable
fun PomodoroNavHost(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Pomodoro.route
    ) {
        composable(Screen.Pomodoro.route){
            val viewModel = viewModel<PomodoroViewModel>()
            PomodoroScreen(
                stateActionManager = viewModel,
                navController = navHostController
            )
        }

        composable(Screen.Preferences.route){
            val viewModel = viewModel<PreferencesVm>()
            PreferencesScreen(
                viewModel = viewModel,
                navHostController = navHostController
            )
        }
    }
}