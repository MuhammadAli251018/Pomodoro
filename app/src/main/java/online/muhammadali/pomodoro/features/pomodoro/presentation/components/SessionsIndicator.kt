package online.muhammadali.pomodoro.features.pomodoro.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import online.muhammadali.pomodoro.common.ui.theme.PomodoroTheme

private fun DrawScope.drawSessionIndicator(
    completed: Boolean,
    color: Color,
    center: Offset,
    radius: Float
) {
    drawCircle(
        color = color,
        center = center,
        radius = radius,
        style = if (completed) Fill else Stroke(width = 3f)
    )
}

@Composable
fun SessionsIndicator(
    modifier: Modifier,
    sessionsCount: Int,
    lastCompleted: Int,
    diameterLength: Dp = 20.dp,
    spaceLength: Dp = 20.dp
) {
    // todo check if sessions numbers is bigger than 8
    val diameter: Float
    val space: Float
    val height: Dp

    LocalDensity.current.apply {
        diameter = diameterLength.toPx()
        space = spaceLength.toPx()
        height = (diameter * 3f / 2f).toDp()
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {

        // offset for center of first circle
        val firstCenter = Offset(x = center.x - ((sessionsCount.toFloat() / 2f) * diameter + ((sessionsCount.toFloat() / 2f) - 1f) * space), y = center.y)

        for (i in 0 until  sessionsCount)
            drawSessionIndicator(
                completed = i <= lastCompleted,
                color = Color.White,
                center = firstCenter.copy(x = firstCenter.x + (diameter + space) * i),
                radius = diameter / 2f
            )
    }
}


@Preview(showBackground = true)
@Composable
fun SessionsIndicatorPreview() {
    PomodoroTheme{
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondary),
            contentAlignment = Alignment.Center
        ) {
            SessionsIndicator(
                modifier = Modifier.fillMaxWidth(),
                5,
                0)
        }
    }
}