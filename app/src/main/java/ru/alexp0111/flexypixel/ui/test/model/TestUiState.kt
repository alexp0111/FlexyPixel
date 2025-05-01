package ru.alexp0111.flexypixel.ui.test.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class TestUiState(
    val totalAmountOfPanels: Int?,
    val testPixel: TestPixelModel,
    val testFrames: TestFramesModel,
    val terminalInput: String,
    val terminalMessages: List<TestTerminalMessage>,
)

internal data class TestPixelModel(
    val r: Int? = null,
    val g: Int? = null,
    val b: Int? = null,
    val panel: Int? = null,
    val pixel: Int? = null,
)

internal data class TestFramesModel(
    val framesAmount: Int? = null,
    val interframeDelay: Int? = null,
)

internal data class TestTerminalMessage(
    val text: String,
    val type: TestTerminalMessageType,
)

internal enum class TestTerminalMessageType {
    REQUEST,
    RESPONSE_ERROR,
    RESPONSE_SUCCESS;
}