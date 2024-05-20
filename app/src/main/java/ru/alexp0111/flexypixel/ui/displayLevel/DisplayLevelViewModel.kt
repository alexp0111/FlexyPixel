package ru.alexp0111.flexypixel.ui.displayLevel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Stack

import javax.inject.Inject

class DisplayLevelViewModel @Inject constructor() : ViewModel() {

    var displayLocationInMatrix =
        MutableStateFlow(MutableList(MAX_DISPLAY_NUMBER) { EMPTY_CELL })
    var displayLocationInHolder = MutableStateFlow(Stack<Int>())
    var bitmapMap: MutableStateFlow<MutableMap<Int, Bitmap>> = MutableStateFlow(mutableMapOf())

    companion object {
        const val HOLDER_POSITION = -1
        const val EMPTY_CELL = 0
        const val MAX_DISPLAY_NUMBER = 9
        const val DESTINATION_IS_HOLDER = -1
    }

    fun getPanelsConfiguration() {
        getDisplayMatrix()
        getDisplayHolder()
        getDisplayImages()
    }

    fun fromHolderToMatrix(destinationPosition: Int) {
        val panelNumber = displayLocationInHolder.value.pop()
        displayLocationInMatrix.value[destinationPosition] =
            panelNumber
    }

    fun fromMatrixToMatrix(ownerPosition: Int, destinationPosition: Int) {
        displayLocationInMatrix.value[destinationPosition] =
            displayLocationInMatrix.value[ownerPosition]
        displayLocationInMatrix.value[ownerPosition] = 0
    }

    fun fromMatrixToHolder(ownerPosition: Int) {
        val panelNumber = displayLocationInMatrix.value[ownerPosition]
        displayLocationInMatrix.value[ownerPosition] = 0
        displayLocationInHolder.value.push(panelNumber)
    }

    private fun getDisplayImages() {
        //TODO getDisplayMatrixValue
    }

    private fun getDisplayMatrix() {
        val newDisplayLocationInMatrix = MutableStateFlow(displayLocationInMatrix.value)
        //test logic
        var counter = 0
        for ((index, i) in newDisplayLocationInMatrix.value.withIndex()) {
            if (index % 3 == 0) {
                newDisplayLocationInMatrix.value[index] = counter
                counter++
            }
        }
        //end of test logic
        displayLocationInMatrix = newDisplayLocationInMatrix
        //TODO getDisplayMatrixValue
    }

    private fun getDisplayHolder() {

        val newDisplayLocationInHolder = MutableStateFlow(displayLocationInHolder.value)
        //test logic
        for (i in 9 downTo displayLocationInMatrix.value.count { it > 0 } + 1) {
            newDisplayLocationInHolder.value.push(i)
        }
        //end of test logic
        displayLocationInHolder = newDisplayLocationInHolder
        //TODO getDisplayHolderValue

    }

    fun sendPanelsConfiguration(segmentNum: Int = 0) {
        val configuration = getPanelsAbsoluteConfiguration(segmentNum)
        //TODO send config to globalStateHolder
    }

    private fun getPanelsAbsoluteConfiguration(segmentNum: Int): MutableList<Pair<Int, Pair<Int, Int>>> {
        val configuration = mutableListOf<Pair<Int, Pair<Int, Int>>>()
        for ((index, panel) in displayLocationInMatrix.value.withIndex()) {
            if (panel == EMPTY_CELL) continue
            val coordinates = getAbsoluteCoordinates(index, segmentNum)
            configuration.add(Pair(panel, coordinates))
        }
        return configuration
    }

    private fun getAbsoluteCoordinates(index: Int, segmentNum: Int): Pair<Int, Int> {
        val xOffset = segmentNum % 3 * 3
        val yOffset = (segmentNum / 3) * 3
        val x = index % 3 + xOffset
        val y = index / 3 + yOffset
        return Pair(x, y)
    }


    //test function
    override fun toString(): String {
        var s = displayLocationInMatrix.value.toString()
        s += displayLocationInHolder.value.reversed().toString()
        return s
    }

}

