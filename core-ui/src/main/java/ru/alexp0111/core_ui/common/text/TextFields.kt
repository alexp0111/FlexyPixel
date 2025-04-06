package ru.alexp0111.core_ui.common.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import ru.alexp0111.core_ui.theme.AppTheme
import ru.alexp0111.core_ui.theme.flexyPixelTypography

@Composable
fun LargeTextField(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = flexyPixelTypography.displayLarge,
    align: TextAlign = TextAlign.Start,
    color: Color = Color.Unspecified,
    weight: FontWeight = FontWeight.Bold,
) =
    Text(
        modifier = modifier,
        text = text,
        color = color,
        style = style.copy(textAlign = align),
        fontWeight = weight,
    )

@Composable
fun MediumTextField(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = flexyPixelTypography.displayMedium,
    align: TextAlign = TextAlign.Start,
    color: Color = Color.Unspecified,
    weight: FontWeight = FontWeight.Normal,
) =
    Text(
        modifier = modifier,
        text = text,
        color = color,
        style = style.copy(textAlign = align),
        fontWeight = weight,
    )

@Composable
fun SmallTextField(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = flexyPixelTypography.displaySmall,
    align: TextAlign = TextAlign.Start,
    color: Color = Color.Unspecified,
    weight: FontWeight = FontWeight.Normal,
) =
    Text(
        modifier = modifier,
        text = text,
        color = color,
        style = style.copy(textAlign = align),
        fontWeight = weight,
    )

@Preview
@Composable
private fun TextFieldPreview() {
    AppTheme {
        Column(Modifier.fillMaxSize()) {
            LargeTextField("LargeTextField", Modifier.fillMaxWidth())
            MediumTextField("MediumTextField", Modifier.fillMaxWidth())
            SmallTextField("SmallTextField", Modifier.fillMaxWidth())
        }
    }
}