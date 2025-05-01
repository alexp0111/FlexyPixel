package ru.alexp0111.flexypixel.ui.test.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.alexp0111.core_ui.common.text.LargeTextField
import ru.alexp0111.core_ui.theme.AppTheme
import ru.alexp0111.core_ui.theme.beigeDark
import ru.alexp0111.core_ui.theme.black
import ru.alexp0111.flexypixel.ui.test.TestViewModel
import ru.alexp0111.flexypixel.ui.test.model.TestFramesModel
import ru.alexp0111.flexypixel.ui.test.model.TestIntent
import ru.alexp0111.flexypixel.ui.test.model.TestPixelModel
import ru.alexp0111.flexypixel.ui.test.model.TestTerminalMessage
import ru.alexp0111.flexypixel.ui.test.model.TestTerminalMessageType
import ru.alexp0111.flexypixel.ui.test.model.TestUiState

@Composable
internal fun TestScreen(
    viewModel: TestViewModel,
) {
    val uiState = viewModel.uiState.collectAsState()
    val intentHandler by rememberUpdatedState(viewModel::sendIntent)

    TestScreenContent(uiState, intentHandler)
}

@Composable
private fun TestScreenContent(
    uiState: State<TestUiState>,
    intentHandler: (TestIntent) -> Unit = {}
) {
    var isTerminalOn by remember { mutableStateOf(false) }
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
                TerminalScreen(uiState, intentHandler)
            } else {
                DebugCardsScreen(uiState, intentHandler)
            }
        }
    }
}

@Preview
@Composable
private fun TestScreenContentPreview() {
    AppTheme {
        val state = remember { mutableStateOf(getPreviewTestState()) }
        TestScreenContent(state)
    }
}

internal fun getPreviewTestState(): TestUiState =
    TestUiState(
        totalAmountOfPanels = 4,
        testPixel = TestPixelModel(6, 6, 6, 0, 63),
        testFrames = TestFramesModel(100, 600),
        terminalInput = "{\"CONF\":9\"}",
        terminalMessages = getPreviewList(),
    )

private fun getPreviewList(): List<TestTerminalMessage> =
    listOf(
        TestTerminalMessage("CONF:5,6,7,8", TestTerminalMessageType.REQUEST),
        TestTerminalMessage("SUCCES", TestTerminalMessageType.RESPONSE_SUCCESS),
        TestTerminalMessage(
            "VERY LONG 123, 345, 34,5 12,4 1,4 ,1 4,23 2,2,5 ,34 5, 34, ,34 6, ,6 3 6,43 6,3 5, 3,24 ,4",
            TestTerminalMessageType.REQUEST
        ),
        TestTerminalMessage("CONF:5,6,7,8", TestTerminalMessageType.REQUEST),
        TestTerminalMessage("ERROR", TestTerminalMessageType.RESPONSE_ERROR),
        TestTerminalMessage(
            "VERY LONG 123, 345, 34,5 12,4 1,4 ,1 4,23 2,2,5 ,34 5, 34, ,34 6, ,6 3 6,43 6,3 5, 3,24 ,4",
            TestTerminalMessageType.REQUEST
        ),
        TestTerminalMessage("CONF:5,6,7,8", TestTerminalMessageType.REQUEST),
        TestTerminalMessage("SUCCES", TestTerminalMessageType.RESPONSE_SUCCESS),
        TestTerminalMessage(
            "VERY LONG 123, 345, 34,5 12,4 1,4 ,1 4,23 2,2,5 ,34 5, 34, ,34 6, ,6 3 6,43 6,3 5, 3,24 ,4",
            TestTerminalMessageType.REQUEST
        ),
        TestTerminalMessage("CONF:5,6,7,8", TestTerminalMessageType.REQUEST),
        TestTerminalMessage("SUCCES", TestTerminalMessageType.RESPONSE_SUCCESS),
        TestTerminalMessage(
            "VERY LONG 123, 345, 34,5 12,4 1,4 ,1 4,23 2,2,5 ,34 5, 34, ,34 6, ,6 3 6,43 6,3 5, 3,24 ,4",
            TestTerminalMessageType.REQUEST
        ),
        TestTerminalMessage("CONF:5,6,7,8", TestTerminalMessageType.REQUEST),
        TestTerminalMessage("SUCCES", TestTerminalMessageType.RESPONSE_SUCCESS),
        TestTerminalMessage(
            "VERY LONG 123, 345, 34,5 12,4 1,4 ,1 4,23 2,2,5 ,34 5, 34, ,34 6, ,6 3 6,43 6,3 5, 3,24 ,4",
            TestTerminalMessageType.REQUEST
        ),
        TestTerminalMessage("CONF:5,6,7,8", TestTerminalMessageType.REQUEST),
        TestTerminalMessage("SUCCES", TestTerminalMessageType.RESPONSE_SUCCESS),
        TestTerminalMessage(
            "VERY LONG 123, 345, 34,5 12,4 1,4 ,1 4,23 2,2,5 ,34 5, 34, ,34 6, ,6 3 6,43 6,3 5, 3,24 ,4",
            TestTerminalMessageType.REQUEST
        ),
    )
