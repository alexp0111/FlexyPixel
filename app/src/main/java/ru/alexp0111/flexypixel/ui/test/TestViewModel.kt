package ru.alexp0111.flexypixel.ui.test

import ru.alexp0111.core.viewmodel.MVIViewModel
import ru.alexp0111.flexypixel.ui.test.model.TestFramesModel
import ru.alexp0111.flexypixel.ui.test.model.TestIntent
import ru.alexp0111.flexypixel.ui.test.model.TestPixelModel
import ru.alexp0111.flexypixel.ui.test.model.TestTerminalMessage
import ru.alexp0111.flexypixel.ui.test.model.TestUiState
import javax.inject.Inject

internal class TestViewModel @Inject constructor(

) : MVIViewModel<TestIntent, TestUiState, Nothing>() {
    override fun setInitialState(): TestUiState =
        TestUiState(
            totalAmountOfPanels = null,
            testPixel = TestPixelModel(),
            testFrames = TestFramesModel(),
            terminalInput = "",
            terminalMessages = emptyList(),
        )

    override fun handleIntent(intent: TestIntent) =
        when (intent) {
            is TestIntent.SendConfig -> onSendConfig(intent.totalPanels)
            is TestIntent.SendPixel -> onSendPixel(intent.testPixelModel)
            is TestIntent.SendFrames -> onSendFrames(intent.testFramesModel)
            TestIntent.PickMedia -> onPickMedia()
            TestIntent.SendMessage -> onSendMessage()
            is TestIntent.TerminalInputChanged -> onTerminalInputChanged(intent.input)
            is TestIntent.TerminalMessageClicked -> onTerminalMessageClicked(intent.message)
        }


    private fun onSendConfig(totalPanels: Int?) {
        setState { copy(totalAmountOfPanels = totalPanels) }
    }

    private fun onSendPixel(testPixelModel: TestPixelModel) {
        setState { copy(testPixel = testPixelModel) }
    }

    private fun onSendFrames(testFramesModel: TestFramesModel) {
        setState { copy(testFrames = testFramesModel) }
    }

    private fun onTerminalInputChanged(input: String) {
        setState { copy(terminalInput = input) }
    }

    private fun onSendMessage() {
        // todo
    }

    private fun onTerminalMessageClicked(message: TestTerminalMessage) {
        setState { copy(terminalInput = message.text) }
    }

    private fun onPickMedia() {
        // todo
    }

}