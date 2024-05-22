package ru.alexp0111.flexypixel.ui.drawing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.alexp0111.flexypixel.ui.GlobalStateHandler
import ru.alexp0111.flexypixel.ui.GlobalStateHandlerFactory

@AssistedFactory
interface DrawingViewModelFactory {
    fun create(schemeId: Int? = null): DrawingViewModel
}

class DrawingViewModel @AssistedInject constructor(
    @Assisted private val schemeId: Int?,
    globalStateHandlerFactory: GlobalStateHandlerFactory,
) : ViewModel(), DrawingActionConsumer, StateHolder {

    private val globalStateHandler =
        GlobalStateHandler.getInstance(globalStateHandlerFactory, schemeId)

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

    fun setPanelPosition(panelPosition: Int) {
        _uiState.update {
            it.copy(
                panelPosition = panelPosition,
            )
        }
    }

    private fun requestDisplayConfiguration() {
        val panelPosition = state.value.panelPosition
        viewModelScope.launch(Dispatchers.IO) {
            val pixelsUiState = DrawingUIState(
                globalStateHandler.getPanelPixelsConfiguration(panelPosition),
                globalStateHandler.getPanelPalette(panelPosition),
            )
            consumeAction(DrawingAction.LoadDisplayConfiguration(pixelsUiState))
        }
    }

    private fun requestPaletteItemUpdate(colorChannel: ColorChannel, colorChannelValue: Int) {
        val panelPosition = state.value.panelPosition
        val newPalette = state.value.palette.toMutableList()
        val chosenPaletteItem = state.value.chosenPaletteItem
        newPalette[chosenPaletteItem] = when (colorChannel) {
            ColorChannel.RED ->
                newPalette[chosenPaletteItem].copy(r = colorChannelValue)

            ColorChannel.GREEN ->
                newPalette[chosenPaletteItem].copy(g = colorChannelValue)

            ColorChannel.BLUE ->
                newPalette[chosenPaletteItem].copy(b = colorChannelValue)
        }
        globalStateHandler.setPanelPalette(panelPosition, newPalette)
        consumeAction(DrawingAction.PaletteItemColorUpdatedSuccessfully(newPalette))
    }

    private fun requestPixelColorUpdate(pixelPosition: Int) {
        val panelPosition = state.value.panelPosition
        val newColor = state.value.getCurrentDrawingColor()
        globalStateHandler.updatePixelColorOnPosition(
            panelPosition,
            newColor,
            pixelPosition,
        )
        consumeAction(DrawingAction.PixelColorUpdatedSuccessfully(newColor, pixelPosition))
    }

    override fun consumeAction(action: DrawingAction) {
        _actions.tryEmit(action)
    }

    private fun dispatchAction(state: DrawingUIState, action: DrawingAction) {
        when (action) {
            is DrawingAction.RequestDisplayConfiguration -> requestDisplayConfiguration()
            is DrawingAction.LoadDisplayConfiguration -> Unit
            is DrawingAction.PickPaletteItem -> Unit
            is DrawingAction.RequestChangePaletteItemColor -> requestPaletteItemUpdate(
                action.colorChannel,
                action.colorChannelValue
            )

            is DrawingAction.PaletteItemColorUpdatedSuccessfully -> Unit
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

            is DrawingAction.RequestChangePaletteItemColor -> state

            is DrawingAction.PaletteItemColorUpdatedSuccessfully -> {
                state.copy(
                    palette = action.newPalette
                )
            }

            is DrawingAction.RequestPixelColorUpdate -> state
            is DrawingAction.PixelColorUpdatedSuccessfully -> {
                val newPixelPanel = state.pixelPanel.toMutableList()
                newPixelPanel[action.pixelPosition] = action.color
                state.copy(
                    pixelPanel = newPixelPanel
                )
            }
        }
    }
}