package online.muhammadali.pomodoro.features.pomodoro.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import online.muhammadali.pomodoro.common.ui.theme.PomodoroTheme



@Composable
fun PomodoroCounter(
    modifier: Modifier,
    size: DpSize,
    time: String,
    completion: Float,
    fontSize: TextUnit,
    backgroundColor: Color
) {
    Box(
        modifier = modifier
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ){
        PomodoroCounter(
            modifier = Modifier,
            size = size,
            time = time,
            completion = completion,
            fontSize = fontSize,
            fontWeight = FontWeight.Light
        )
    }
}

// stateless
@Composable
fun PomodoroCounter(
    modifier: Modifier,
    size: DpSize,
    time: String,
    completion: Float,
    fontSize: TextUnit,
    fontWeight: FontWeight
) {
    Box(
        modifier = modifier
            .size(size),
        contentAlignment = Alignment.Center
    ) {
        Text(
            color = MaterialTheme.colorScheme.primary,
            text = time,
            fontSize = fontSize,
            fontWeight = fontWeight
        )

        val arcColor = MaterialTheme.colorScheme.primary

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val radius = this.size.width * 0.9f

            drawArc(
                color = arcColor,
                startAngle = 0f,
                sweepAngle = completion * 360f,
                useCenter = false,
                topLeft = Offset(x = (this.size.width - radius) / 2, y = (this.size.width - radius) / 2),
                size = Size(radius, radius),
                style = Stroke(
                    width = 6f,
                    cap = StrokeCap.Square
                )
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PomodoroCounterPreview() {
    PomodoroTheme {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.secondary),
            contentAlignment = Alignment.Center
        ){
            PomodoroCounter(
                modifier = Modifier,
                size = DpSize(150.dp, 150.dp),
                time = "25:00",
                completion = .75f,
                fontSize = 35.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}