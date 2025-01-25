package ru.alexp0111.core_ui.common.text

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
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
    style: TextStyle = flexyPixelTypography.displayLarge,
    imeAction: ImeAction = ImeAction.Done,
    singleLine: Boolean = true,
    onValueChanged: (String) -> Unit,
) {
    TextField(
        value = initialText,
        textStyle = style,
        singleLine = singleLine,
        onValueChange = onValueChanged,
        keyboardOptions =  KeyboardOptions(imeAction = imeAction),
        colors = TextFieldDefaults.colors().copy(
            unfocusedContainerColor = flexyPixelColorScheme.background,
            focusedContainerColor = flexyPixelColorScheme.background,
            disabledContainerColor = flexyPixelColorScheme.background,
            cursorColor = black,
            errorCursorColor = black,
            focusedIndicatorColor = black,
            errorIndicatorColor = black,
            unfocusedIndicatorColor = black,
            disabledIndicatorColor = black,
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
            LargeEditTextField("MyText") {}
        }
    }
}