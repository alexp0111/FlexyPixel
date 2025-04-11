package ru.alexp0111.core_ui.common.text

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.alexp0111.core_ui.theme.AppTheme
import ru.alexp0111.core_ui.theme.beigeDark
import ru.alexp0111.core_ui.theme.black
import ru.alexp0111.core_ui.theme.flexyPixelColorScheme
import ru.alexp0111.core_ui.theme.flexyPixelTypography

@Composable
fun LargeEditTextField(
    initialText: String = "",
    hint: String = "",
    modifier: Modifier = Modifier,
    style: TextStyle = flexyPixelTypography.displayLarge,
    align: TextAlign = TextAlign.Start,
    imeAction: ImeAction = ImeAction.Done,
    inputType: KeyboardType = KeyboardType.Unspecified,
    singleLine: Boolean = true,
    indicatorColor: Color = black,
    onValueChanged: (String) -> Unit,
) {
    TextField(
        modifier = modifier,
        value = initialText,
        placeholder = { LargeTextField(text = hint, modifier = modifier, align = align, color = beigeDark) },
        textStyle = style.copy(textAlign = align),
        singleLine = singleLine,
        onValueChange = onValueChanged,
        keyboardOptions = KeyboardOptions(imeAction = imeAction, keyboardType = inputType),
        colors = TextFieldDefaults.colors().copy(
            unfocusedContainerColor = flexyPixelColorScheme.background,
            focusedContainerColor = flexyPixelColorScheme.background,
            disabledContainerColor = flexyPixelColorScheme.background,
            cursorColor = black,
            errorCursorColor = black,
            focusedIndicatorColor = indicatorColor,
            errorIndicatorColor = indicatorColor,
            unfocusedIndicatorColor = indicatorColor,
            disabledIndicatorColor = indicatorColor,
            textSelectionColors = TextSelectionColors(
                handleColor = black,
                backgroundColor = beigeDark,
            ),
        )
    )
}

@Composable
fun MediumEditTextField(
    initialText: String = "",
    hint: String = "",
    modifier: Modifier = Modifier,
    style: TextStyle = flexyPixelTypography.displayMedium,
    align: TextAlign = TextAlign.Start,
    imeAction: ImeAction = ImeAction.Done,
    inputType: KeyboardType = KeyboardType.Unspecified,
    singleLine: Boolean = true,
    indicatorColor: Color = black,
    onValueChanged: (String) -> Unit,
) {
    TextField(
        modifier = modifier,
        value = initialText,
        placeholder = { MediumTextField(text = hint, modifier = modifier, align = align, color = beigeDark) },
        textStyle = style.copy(textAlign = align),
        singleLine = singleLine,
        onValueChange = onValueChanged,
        keyboardOptions = KeyboardOptions(imeAction = imeAction, keyboardType = inputType),
        colors = TextFieldDefaults.colors().copy(
            unfocusedContainerColor = flexyPixelColorScheme.background,
            focusedContainerColor = flexyPixelColorScheme.background,
            disabledContainerColor = flexyPixelColorScheme.background,
            cursorColor = black,
            errorCursorColor = black,
            focusedIndicatorColor = indicatorColor,
            errorIndicatorColor = indicatorColor,
            unfocusedIndicatorColor = indicatorColor,
            disabledIndicatorColor = indicatorColor,
            textSelectionColors = TextSelectionColors(
                handleColor = black,
                backgroundColor = beigeDark,
            ),
        )
    )
}

@Preview
@Composable
private fun LargeEditTextFieldPreview() {
    AppTheme {
        Box(Modifier.padding(32.dp)) {
            LargeEditTextField(hint = "MyText") {}
        }
    }
}

@Preview
@Composable
private fun MediumEditTextFieldPreview() {
    AppTheme {
        Box(Modifier.padding(32.dp)) {
            MediumEditTextField(hint = "MyText", modifier = Modifier.fillMaxWidth()) {}
        }
    }
}