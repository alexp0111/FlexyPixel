package ru.alexp0111.flexypixel.ui.test.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.alexp0111.core_ui.common.text.LargeTextField
import ru.alexp0111.core_ui.theme.AppTheme
import ru.alexp0111.core_ui.theme.beigeDark
import ru.alexp0111.core_ui.theme.black
import ru.alexp0111.flexypixel.ui.test.TestViewModel

@Composable
internal fun TestScreen(
    viewModel: TestViewModel,
) {
    // val uiState = viewModel.uiState.collectAsState()
    // val intentHandler by rememberUpdatedState(viewModel::sendIntent)

    TestScreenContent()
}

@Composable
private fun TestScreenContent() {
    var isTerminalOn by remember { mutableStateOf(true) }
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
            ) {
                LargeTextField(
                    text = "Actions",
                    color = if (isTerminalOn) beigeDark else black,
                    modifier = Modifier.clickable(interactionSource = null, indication = null) {
                        isTerminalOn = false
                    }
                )
                LargeTextField(text = " / ")
                LargeTextField(
                    text = "CMD",
                    color = if (isTerminalOn) black else beigeDark,
                    modifier = Modifier.clickable(interactionSource = null, indication = null) {
                        isTerminalOn = true
                    }
                )
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            if (isTerminalOn) {
                TerminalScreen(emptyList())
            } else {
                DebugCardsScreen()
            }
        }
    }
}

@Preview
@Composable
private fun TestScreenContentPreview() {
    AppTheme {
        TestScreenContent()
    }
}
