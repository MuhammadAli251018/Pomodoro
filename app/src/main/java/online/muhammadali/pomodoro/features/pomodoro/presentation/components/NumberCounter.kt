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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import online.muhammadali.pomodoro.common.presentation.components.VerticalSpace
import online.muhammadali.pomodoro.common.ui.theme.PomodoroTheme

/*** Stateful*/


@Composable
fun NumberCounter(
    modifier: Modifier,
    min: Int,
    max: Int,
    current: Int,
    step: Int,
    title: String,
    backgroundColor: Color,
    titleFontSize: TextUnit,
    numberFontSize: TextUnit,
    onChange: (Int) -> Unit
) {
    var showControllers by remember { mutableStateOf(false) }
    var currentCount by remember { mutableIntStateOf(current) }

    LaunchedEffect(current) {
        currentCount = current
    }

    NumberCounter(
        modifier = modifier,
        current = currentCount,
        backgroundColor = backgroundColor,
        showControllers = showControllers,
        title = title,
        onNumberClick = {showControllers = !showControllers},
        bigTextSize = numberFontSize,
        titleFontSize = titleFontSize,
        smallTextSize = numberFontSize / 2,
        onIncrement = {
            if ((currentCount + step) <= max)
            {
                currentCount += step
                onChange(currentCount)
            }
        },
        onDecrement = {
            if ((currentCount - step) >= min) {
                currentCount -= step
                onChange(currentCount)
            }
        }
    )
}

/***  Stateless*/
@Composable
fun NumberCounter(
    modifier: Modifier,
    current: Int,
    backgroundColor: Color,
    showControllers: Boolean,
    title: String,
    bigTextSize: TextUnit,
    smallTextSize: TextUnit,
    titleFontSize: TextUnit,
    onNumberClick: () -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Column(
        modifier = modifier
            .background(backgroundColor)
    ){
        VerticalSpace(height = 10.dp)

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 10.dp),
            text = title,
            fontSize = titleFontSize,
            color = Color.White
        )

        VerticalSpace(height = 10.dp)

        Row(
            modifier
                .background(backgroundColor.makeDarker(.15f))
        ) {

            if (showControllers) {
                CounterController(
                    modifier = Modifier.weight(0.5f),
                    backgroundColor = Color.Transparent,
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
                backgroundColor = Color.Transparent,
                onClick = onNumberClick,
                fontSize = bigTextSize
            )
        }
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
            titleFontSize = 20.sp,
            title = "bla",
            showControllers = true,
            bigTextSize = 60.sp,
            smallTextSize = 40.sp,
            onNumberClick = {  },
            onIncrement = {  }) {

        }
    }
}

private fun Color.makeDarker(percentage: Float): Color {
    val newRed = (Color.Black.red - red) * percentage + red
    val newGreen = (Color.Black.green - green) * percentage + green
    val newBlue = (Color.Black.blue - blue) * percentage + blue

    return Color(red = newRed, green = newGreen, blue = newBlue)
}