package ru.alexp0111.core_ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.nikhilchaudhari.library.neumorphic
import me.nikhilchaudhari.library.shapes.Pressed
import ru.alexp0111.core_ui.theme.AppTheme
import ru.alexp0111.core_ui.theme.beigeDark
import ru.alexp0111.core_ui.theme.beigeStandard
import ru.alexp0111.core_ui.theme.whiteShadow

@Composable
fun NeoSlot(
    modifier: Modifier,
    strokeWidth: Dp = 6.dp,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Card(
        onClick = onClick,
        enabled = false,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(disabledContainerColor = beigeStandard),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .neumorphic(
                neuShape = Pressed.Rounded(24.dp),
                strokeWidth = strokeWidth,
                lightShadowColor = whiteShadow,
                darkShadowColor = beigeDark,
            )
    ) {
        content.invoke()
    }
}

@Preview
@Composable
private fun NeoCardPreview() {
    AppTheme {
        NeoSlot(
            modifier = Modifier
                .padding(36.dp)
                .height(128.dp),
            onClick = {},
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "Example")
            }
        }
    }
}