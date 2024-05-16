package ru.alexp0111.flexypixel.ui.drawing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DrawingViewModel @Inject constructor() : ViewModel(), DrawingActionConsumer, StateHolder {

    private val _actions: MutableSharedFlow<DrawingAction> by lazy {
        MutableSharedFlow(extraBufferCapacity = 1)
    }

    private val _uiState = MutableStateFlow(DrawingUIState())
    override val state: StateFlow<DrawingUIState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _actions.collect { action ->
                val currentState = _uiState.value
                _uiState.value = reduce(state = currentState, action = action)
                viewModelScope.launch {
                    dispatchAction(state = currentState, action = action)
                }
            }
        }
    }

    private fun requestDisplayConfiguration(displayPosition: Int) {
        /*TODO request uiState from GlobalStateHandler
        consumeAction(DrawingAction.LoadDisplayConfiguration(uiState))
         */
    }

    private fun requestPixelColorUpdate(pixelPosition: Int) {
        //TODO collect Flow from GlobalStateHandler
        consumeAction(DrawingAction.PixelColorUpdatedSuccessfully(pixelPosition))
    }

    override fun consumeAction(action: DrawingAction) {
        _actions.tryEmit(action)
    }

    private fun dispatchAction(state: DrawingUIState, action: DrawingAction) {
        when (action) {
            is DrawingAction.RequestDisplayConfiguration -> requestDisplayConfiguration(action.displayPosition)
            is DrawingAction.LoadDisplayConfiguration -> Unit
            is DrawingAction.ChangePaletteItemColor -> Unit
            is DrawingAction.PickPaletteItem -> Unit
            is DrawingAction.RequestPixelColorUpdate -> requestPixelColorUpdate(action.pixelPosition)
            is DrawingAction.PixelColorUpdatedSuccessfully -> Unit
        }
    }

    private fun reduce(
        state: DrawingUIState,
        action: DrawingAction,
    ): DrawingUIState {
        return when (action) {
            is DrawingAction.RequestDisplayConfiguration -> state
            is DrawingAction.LoadDisplayConfiguration -> action.drawingUIState
            is DrawingAction.PickPaletteItem -> state.copy(
                chosenPaletteItem = action.paletteItemPosition
            )

            is DrawingAction.ChangePaletteItemColor -> {
                val newPalette = state.palette.toMutableList()
                newPalette[state.chosenPaletteItem] = when (action.colorChannel) {
                    ColorChannel.RED ->
                        newPalette[state.chosenPaletteItem].copy(r = action.colorChannelValue)

                    ColorChannel.GREEN ->
                        newPalette[state.chosenPaletteItem].copy(g = action.colorChannelValue)

                    ColorChannel.BLUE ->
                        newPalette[state.chosenPaletteItem].copy(b = action.colorChannelValue)
                }
                state.copy(
                    palette = newPalette
                )
            }

            is DrawingAction.RequestPixelColorUpdate -> state
            is DrawingAction.PixelColorUpdatedSuccessfully -> {
                val newPixelPanel = state.pixelPanel.toMutableList()
                newPixelPanel[action.pixelPosition] = state.getCurrentDrawingColor()
                state.copy(
                    pixelPanel = newPixelPanel
                )
            }
        }
    }
}