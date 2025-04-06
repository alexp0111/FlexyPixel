package ru.alexp0111.flexypixel.ui.test.model

internal sealed interface TestIntent {
    data class SendConfig(val totalPanels: Int?) : TestIntent
    data class SendPixel(val testPixelModel: TestPixelModel) : TestIntent
    data class SendFrames(val testFramesModel: TestFramesModel) : TestIntent
    data object SendMessage : TestIntent
    data class TerminalInputChanged(val input: String) : TestIntent
    data class TerminalMessageClicked(val message: TestTerminalMessage) : TestIntent
    data object PickMedia : TestIntent
}