package online.muhammadali.pomodoro.features.pomodoro.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import online.muhammadali.pomodoro.common.ui.theme.PomodoroTheme

/***  Stateless*/
@Composable
fun NumberCounter(
    modifier: Modifier,
    current: Int,
    backgroundColor: Color,
    showControllers: Boolean,
    onNumberClick: () -> Unit,
    bigTextSize: TextUnit,
    smallTextSize: TextUnit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Row(
        modifier = modifier
            .background(backgroundColor)
    ) {

        if (showControllers){
            CounterController(
                modifier = Modifier.weight(0.5f),
                backgroundColor = backgroundColor,
                onIncrement = onIncrement,
                onDecrement = onDecrement,
                fontSize = smallTextSize
            )
        }

        CountedNumber(
            modifier = Modifier
                .weight(0.5f)
                .align(Alignment.CenterVertically),
            current = current,
            backgroundColor = backgroundColor,
            onClick = onNumberClick,
            fontSize = bigTextSize
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CountedNumber(
    modifier: Modifier,
    current: Int,
    backgroundColor: Color,
    fontSize: TextUnit = 60.sp,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = current.toString(),
                color = Color.White,
                fontSize = fontSize
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CounterController(
    modifier: Modifier,
    backgroundColor: Color,
    fontSize: TextUnit = 40.sp,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
){
    Column(
        modifier = modifier
    ) {
        Card(
            modifier = Modifier,
            shape = RectangleShape,
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            onClick = onIncrement
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    color = Color.White,
                    fontSize = fontSize
                )
            }
        }

        Card(
            modifier = Modifier,
            shape = RectangleShape,
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            onClick = onDecrement
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "-",
                    color = Color.White,
                    fontSize = fontSize
                )
            }
        }
    }
}

@Preview
@Composable
fun CountedNumberPreview() {
    PomodoroTheme {
        CountedNumber(
            modifier = Modifier
                .fillMaxSize(),
            current = 25,
            backgroundColor = MaterialTheme.colorScheme.background
        ) {

        }
    }
}

@Preview
@Composable
fun CounterControllerPreview() {
    PomodoroTheme {
        CounterController(
            modifier = Modifier
                .fillMaxWidth(),
            backgroundColor = MaterialTheme.colorScheme.background,
            onIncrement = {}
        ) {

        }
    }
}

@Preview
@Composable
fun NumberCounterPreview() {
    PomodoroTheme{
        NumberCounter(
            modifier = Modifier.fillMaxWidth(),
            current = 25,
            backgroundColor = MaterialTheme.colorScheme.background,
            showControllers = true,
            bigTextSize = 60.sp,
            smallTextSize = 40.sp,
            onNumberClick = { /*TODO*/ },
            onIncrement = { /*TODO*/ }) {

        }
    }
}