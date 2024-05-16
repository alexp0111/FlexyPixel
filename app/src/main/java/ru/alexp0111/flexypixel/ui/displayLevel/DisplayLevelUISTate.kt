package ru.alexp0111.flexypixel.ui.displayLevel

import android.graphics.Bitmap
import kotlinx.coroutines.flow.StateFlow
import java.util.LinkedList


const val MAX_DISPLAY_NUMBER = 9


data class DisplayLevelUISTate(
    val displayLocationInMatrix: MutableList<Int> = MutableList(MAX_DISPLAY_NUMBER) { -1 },
    val displayLocationInHolder: LinkedList<Int> = LinkedList<Int>(),
    val runtimeDraggedPanelNumber: Int = -1,
    val bitmapMap: MutableMap<Int,Bitmap> = mutableMapOf(),
    val canBeRepainted: Boolean = true
) {
    fun getSetUpPanelsAmount() = displayLocationInMatrix.count { it > 0 }
    fun getCardPositionInMatrix(panelNumber: Int): Int? {
        return displayLocationInMatrix.find { it == panelNumber }
    }
    companion object{
        const val IS_EMPTY = -1
    }
}



interface StateHolder {
    val state: StateFlow<DisplayLevelUISTate>
}
