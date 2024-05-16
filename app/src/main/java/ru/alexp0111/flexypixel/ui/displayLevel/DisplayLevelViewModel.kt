package ru.alexp0111.flexypixel.ui.displayLevel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import javax.inject.Inject

const val DESTINATION_IS_HOLDER = -1

class DisplayLevelViewModel @Inject constructor() : ViewModel(), DisplayLevelActionConsumer,
    StateHolder {
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
            is DisplayLevelAction.GetCurrentPanelNumberInHolder -> requestCurrentPanelInHolder()
            is DisplayLevelAction.SetCurrentPanelNumberInHolder -> Unit
            is DisplayLevelAction.GetCurrentPanelsInMatrix -> requestCurrentPanelsInMatrix(action.segmentNumber)
            is DisplayLevelAction.SetCurrentPanelsInMatrix -> Unit
            is DisplayLevelAction.GetBitmapsForPanelsByOrder -> requestBitmapsForPanelsByOrder(
                action.segmentNumber
            )

            is DisplayLevelAction.SetBitmapsForPanelsByOrder -> Unit
            is DisplayLevelAction.CardOnDragFromHolder -> Unit
            is DisplayLevelAction.CardOnDragFromMatrix -> Unit
            is DisplayLevelAction.CardOnDropFromHolderToMatrixSuccessfully -> sendNewPanelData(
                action.destinationPosition,
                action.panelNumber
            )

            is DisplayLevelAction.CardOnDropFromMatrixToMatrixSuccessfully -> sendNewPanelData(
                action.destinationPosition,
                action.panelNumber
            )

            is DisplayLevelAction.CardOnDropFromMatrixToHolderSuccessfully -> sendNewPanelData(
                DESTINATION_IS_HOLDER,
                action.panelNumber
            )

            is DisplayLevelAction.CardOnDropFromMatrixFailure -> Unit
            is DisplayLevelAction.CardOnDropFromHolderFailure -> Unit
        }
    }

    //If destination pos is -1 then it's destination is holder
    private fun sendNewPanelData(destinationPosition: Int, panelNumber: Int) {

    }

    private fun requestCurrentPanelInHolder() {

    }

    private fun requestBitmapsForPanelsByOrder(segmentNumber: Int) {

    }


    private fun requestCurrentPanelsInMatrix(segmentNumber: Int) {

    }

    private fun reduce(
        state: DisplayLevelUISTate,
        action: DisplayLevelAction
    ): DisplayLevelUISTate {
        return when (action) {
            is DisplayLevelAction.CardOnDragFromHolder -> state.copy(
                canBeRepainted = false,
                runtimeDraggedPanelNumber = state.displayLocationInHolder.last
            )

            is DisplayLevelAction.CardOnDropFromHolderToMatrixSuccessfully -> {
                val newDisplayLocationInMatrix = state.displayLocationInMatrix.toMutableList()
                newDisplayLocationInMatrix[action.destinationPosition] = action.panelNumber
                val newDisplayLocationInHolder = state.displayLocationInHolder
                newDisplayLocationInHolder.pop()
                state.copy(
                    canBeRepainted = true,
                    runtimeDraggedPanelNumber = DisplayLevelUISTate.IS_EMPTY,
                    displayLocationInHolder = newDisplayLocationInHolder,
                    displayLocationInMatrix = newDisplayLocationInMatrix
                )
            }

            else -> {
                state
            }
        }
    }

}