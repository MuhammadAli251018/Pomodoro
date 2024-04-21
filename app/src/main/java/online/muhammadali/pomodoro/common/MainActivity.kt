package online.muhammadali.pomodoro.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import online.muhammadali.pomodoro.common.ui.theme.PomodoroTheme
import online.muhammadali.pomodoro.features.pomodoro.presentation.screens.PomodoroScreen
import online.muhammadali.pomodoro.features.pomodoro.presentation.screens.PomodoroViewModel
import online.muhammadali.pomodoro.features.pomodoro.presentation.screens.navigation.PomodoroNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomodoroTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    /*val viewModel = viewModel<PomodoroViewModel>()
                    PomodoroScreen(stateActionManager = viewModel)*/

                    PomodoroNavHost(navHostController = rememberNavController())
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PomodoroTheme {
        Greeting("Android")
    }
}