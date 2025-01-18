package ru.alexp0111.flexypixel.ui.core

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.nikhilchaudhari.library.NeuInsets
import me.nikhilchaudhari.library.neumorphic
import me.nikhilchaudhari.library.shapes.Punched
import ru.alexp0111.flexypixel.ui.theme.AppTheme
import ru.alexp0111.flexypixel.ui.theme.beigeDark
import ru.alexp0111.flexypixel.ui.theme.beigeStandard
import ru.alexp0111.flexypixel.ui.theme.whiteShadow

@Composable
fun NeoCard(
    modifier: Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = beigeStandard),
        modifier = modifier
            .fillMaxWidth()
            .neumorphic(
                neuShape = Punched.Rounded(28.dp),
                neuInsets = NeuInsets(6.dp, 6.dp),
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
        NeoCard(
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