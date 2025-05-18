package ru.alexp0111.core_ui.common.shimmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import ru.alexp0111.core_ui.theme.AppTheme
import ru.alexp0111.core_ui.theme.beigeDark
import ru.alexp0111.core_ui.theme.beigeStandard
import ru.alexp0111.core_ui.util.conditional

@Composable
fun ShimmeringItem(
    cornerRadius: Dp = 1000.dp,
    shimmerWidth: Dp? = null,
    shimmerHeight: Dp? = null,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    Card(
        modifier = Modifier
            .padding(paddingValues)
            .conditional(shimmerWidth == null) { fillMaxWidth() }
            .conditional(shimmerWidth != null) { width(shimmerWidth!!) }
            .conditional(shimmerHeight == null) { fillMaxHeight() }
            .conditional(shimmerHeight != null) { height(shimmerHeight!!) }
            .shimmer(),
        shape = RoundedCornerShape(cornerRadius),
    ) {
        Box(Modifier.fillMaxSize().background(beigeDark))
    }
}

@Preview
@Composable
private fun ShimmeringItemPreview() {
    AppTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(beigeStandard)) {
            ShimmeringItem(
                shimmerWidth = 200.dp,
                shimmerHeight = 50.dp,
                cornerRadius = 25.dp,
            )
        }
    }
}