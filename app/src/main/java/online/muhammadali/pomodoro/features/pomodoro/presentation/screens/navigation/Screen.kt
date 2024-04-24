package online.muhammadali.pomodoro.features.pomodoro.presentation.screens.navigation

sealed class Screen(val route: String) {
    object Pomodoro : Screen("pomodoro-screen")
    object Preferences : Screen("preferences-screen")
}