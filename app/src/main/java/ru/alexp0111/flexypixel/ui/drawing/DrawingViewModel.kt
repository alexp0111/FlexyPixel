package ru.alexp0111.flexypixel.ui.drawing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.alexp0111.flexypixel.ui.start.device_pairing.Action
import ru.alexp0111.flexypixel.ui.start.device_pairing.UiState

class DrawingViewModel : ViewModel(), DrawingActionConsumer, StateHolder {

    private val _actions: MutableSharedFlow<DrawingAction> by lazy {
        MutableSharedFlow(extraBufferCapacity = 1)
    }

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

    private fun dispatchAction(state: DrawingUIState, action: DrawingAction) {
        when (action){
            is DrawingAction.ChangePaletteItemColor -> changePaletteItemColor()
            is DrawingAction.PickPaletteItem -> pickPaletteItem()
            is DrawingAction.ChangePixelColor -> changePixelColor()
        }
    }
    //Incomplete
    private fun changePixelColor() {

    }
    //Incomplete
    private fun pickPaletteItem() {

    }
    //Incomplete
    private fun changePaletteItemColor() {

    }

    private val _uiState = MutableStateFlow(DrawingUIState())
    override val state: StateFlow<DrawingUIState>
        get() = _uiState.asStateFlow()

    override fun consumeAction(action: DrawingAction) {
        _actions.tryEmit(action)
    }

    //Incomplete
    private fun reduce(
        state: DrawingUIState,
        action: DrawingAction,
    ): DrawingUIState {
        return when (action) {
            is DrawingAction.ChangePixelColor -> state
            is DrawingAction.PickPaletteItem -> state
            is DrawingAction.ChangePaletteItemColor -> state
        }
    }
}