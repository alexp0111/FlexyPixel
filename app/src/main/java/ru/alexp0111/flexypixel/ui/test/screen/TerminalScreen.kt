package ru.alexp0111.flexypixel.ui.test.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import ru.alexp0111.core_ui.R
import ru.alexp0111.core_ui.common.NeoImageButton
import ru.alexp0111.core_ui.common.NeoSlot
import ru.alexp0111.core_ui.common.text.MediumEditTextField
import ru.alexp0111.core_ui.common.text.SmallTextField
import ru.alexp0111.core_ui.theme.AppTheme
import ru.alexp0111.core_ui.theme.beigeDark
import ru.alexp0111.core_ui.theme.beigeStandard
import ru.alexp0111.core_ui.theme.black
import ru.alexp0111.flexypixel.ui.test.model.TestIntent
import ru.alexp0111.flexypixel.ui.test.model.TestTerminalMessageType
import ru.alexp0111.flexypixel.ui.test.model.TestUiState


@Composable
internal fun TerminalScreen(
    uiState: State<TestUiState>,
    intentHandler: (TestIntent) -> Unit = {}
) {
    Column {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(uiState.value.terminalMessages) {
                SmallTextField(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable(interactionSource = null, indication = null) {
                            intentHandler(TestIntent.TerminalMessageClicked(it))
                        },
                    text = it.text,
                    color = when (it.type) {
                        TestTerminalMessageType.REQUEST -> beigeDark
                        TestTerminalMessageType.RESPONSE -> black
                    }
                )
            }
        }
        InputMessageRow(uiState, intentHandler)
    }
}

@Composable
private fun InputMessageRow(
    uiState: State<TestUiState>,
    intentHandler: (TestIntent) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var height by remember { mutableStateOf(IntSize.Zero) }
        NeoSlot(
            modifier = Modifier
                .weight(1f)
                .onGloballyPositioned {
                    height = it.size
                }
        ) {
            MediumEditTextField(
                hint = "JSON message",
                initialText = uiState.value.terminalInput,
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                indicatorColor = beigeStandard,
                onValueChanged = { intentHandler(TestIntent.TerminalInputChanged(it)) },
            )
        }
        NeoImageButton(
            imageId = R.drawable.ic_send,
            scale = 0.8f,
            modifier = Modifier
                .width(64.dp)
                .height(with(LocalDensity.current) { height.height.toDp() }),
            onClick = { intentHandler(TestIntent.SendMessage) }
        )
    }
}

@Preview
@Composable
private fun TerminalScreenPreview() {
    AppTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(beigeStandard)
        ) {
            val state = remember { mutableStateOf(getPreviewTestState()) }
            TerminalScreen(state)
        }
    }
}