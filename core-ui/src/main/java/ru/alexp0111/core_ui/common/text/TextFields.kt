package ru.alexp0111.core_ui.common.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import ru.alexp0111.core_ui.theme.AppTheme
import ru.alexp0111.core_ui.theme.flexyPixelTypography

@Composable
fun LargeTextField(
    text: String,
    weight: FontWeight = FontWeight.Bold,
) =
    Text(
        text = text,
        style = flexyPixelTypography.displayLarge,
        fontWeight = weight,
    )

@Composable
fun MediumTextField(
    text: String,
    weight: FontWeight = FontWeight.Normal,
) =
    Text(
        text = text,
        style = flexyPixelTypography.displayMedium,
        fontWeight = weight,
    )

@Composable
fun SmallTextField(
    text: String,
    weight: FontWeight = FontWeight.Normal,
) =
    Text(
        text = text,
        style = flexyPixelTypography.displaySmall,
        fontWeight = weight,
    )

@Preview
@Composable
private fun TextFieldPreview() {
    AppTheme {
        Column(Modifier.fillMaxSize()) {
            LargeTextField("LargeTextField")
            MediumTextField("MediumTextField")
            SmallTextField("SmallTextField")
        }
    }
}