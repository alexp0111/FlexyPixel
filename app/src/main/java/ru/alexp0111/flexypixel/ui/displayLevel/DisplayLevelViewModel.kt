package ru.alexp0111.flexypixel.ui.displayLevel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import javax.inject.Inject

class DisplayLevelViewModel @Inject constructor(): ViewModel(),DisplayLevelActionConsumer,StateHolder {
    private val _actions: MutableSharedFlow<DisplayLevelAction> by lazy {
        MutableSharedFlow(extraBufferCapacity = 1)
    }

    private val _uiState = MutableStateFlow(DisplayLevelUISTate())

    override val state: StateFlow<DisplayLevelUISTate> get() = _uiState.asStateFlow()

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
    override fun consumeAction(action: DisplayLevelAction) {
        _actions.tryEmit(action)
    }
    private fun dispatchAction(state: DisplayLevelUISTate, action: DisplayLevelAction) {
        when (action) {

            else -> {}
        }
    }

    private fun reduce(state: DisplayLevelUISTate, action: DisplayLevelAction) : DisplayLevelUISTate{
        return when(action){

            else -> {state}
        }
    }

}