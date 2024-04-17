package online.muhammadali.pomodoro.features.pomodoro.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import online.muhammadali.pomodoro.R


@Composable
fun SettingsButton(
    modifier: Modifier = Modifier,
    iconSize: DpSize = DpSize(width = 40.dp, height = 40.dp),
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Icon(
            modifier = Modifier
                .size(iconSize)
                .clickable(onClick = onClick),
            painter = painterResource(id = R.drawable.preferences_ic),
            contentDescription = "to settings",
            tint = Color.White
        )
    }
}



@Preview(showBackground = true)
@Composable
fun SettingsButtonPreview() {
    SettingsButton {

    }
}
