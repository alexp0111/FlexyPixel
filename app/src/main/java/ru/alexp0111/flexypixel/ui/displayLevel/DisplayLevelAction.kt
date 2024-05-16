package ru.alexp0111.flexypixel.ui.displayLevel

import android.graphics.Bitmap

sealed interface DisplayLevelAction {

    data object GetCurrentPanelNumberInHolder : DisplayLevelAction
    data class SetCurrentPanelNumberInHolder(val currentPanelNumber: Int) : DisplayLevelAction

    /*
    returning list of PanelMetadata from GlobalStateHolder only for this segment
     */
    data class GetCurrentPanelsInMatrix(val segmentNumber: Int) : DisplayLevelAction

    data class SetCurrentPanelsInMatrix(val displayLocationInMatrix: MutableList<Int>) :
        DisplayLevelAction

    /*
    returning map of panel order and bitmaps from GlobalStateHolder only for this segment
     */
    data class GetBitmapsForPanelsByOrder(val segmentNumber: Int) : DisplayLevelAction
    data class SetBitmapsForPanelsByOrder(val bitmapsMap: MutableMap<Int, Bitmap>) :
        DisplayLevelAction

    data object CardOnDragFromHolder : DisplayLevelAction
    data class CardOnDragFromMatrix(val panelNumber: Int) : DisplayLevelAction


    data class CardOnDropFromHolderToMatrixSuccessfully(
        val destinationPosition: Int,
        val panelNumber: Int
    ) : DisplayLevelAction

    data class CardOnDropFromMatrixToMatrixSuccessfully(
        val destinationPosition: Int,
        val panelNumber: Int
    ) : DisplayLevelAction

    data class CardOnDropFromMatrixToHolderSuccessfully(val panelNumber: Int) : DisplayLevelAction
    data class CardOnDropFromMatrixFailure(val rootPosition: Int, val panelNumber: Int) :
        DisplayLevelAction

    data class CardOnDropFromHolderFailure(val panelNumber: Int) : DisplayLevelAction


}

interface DisplayLevelActionConsumer {
    fun consumeAction(action: DisplayLevelAction)
}
