package ru.alexp0111.flexypixel.ui.displayLevel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import java.util.Stack

import javax.inject.Inject

const val DESTINATION_IS_HOLDER = -1

class DisplayLevelViewModel @Inject constructor() : ViewModel() {
    val displayLocationInMatrix: MutableList<Int> =
        MutableList(MAX_DISPLAY_NUMBER) { DisplayLevelViewModel.EMPTY_CELL }
    val displayLocationInHolder = Stack<Int>()
    val bitmapMap: MutableMap<Int, Bitmap> = mutableMapOf()

    companion object {
        const val HOLDER_POSITION = -1
        const val EMPTY_CELL = 0
    }

    init {
        getDisplayMatrix()
        getDisplayHolder()
    }

    fun fromHolderToMatrix(destinationPosition: Int) {
        val panelNumber = displayLocationInHolder.pop();
        displayLocationInMatrix[destinationPosition] =
            panelNumber
    }
    fun fromMatrixToMatrix(ownerPosition: Int, destinationPosition: Int) {
        displayLocationInMatrix[destinationPosition] =
            displayLocationInMatrix[ownerPosition]
        displayLocationInMatrix[ownerPosition] = 0
    }
    fun fromMatrixToHolder(ownerPosition: Int) {
        val panelNumber = displayLocationInMatrix[ownerPosition]
        displayLocationInMatrix[ownerPosition] = 0
        displayLocationInHolder.push(panelNumber)
    }

    private fun getDisplayImages() {
        //test
    }

    private fun getDisplayMatrix() {
        //test logic
        var counter = 0
        for ((index, i) in displayLocationInMatrix.withIndex()) {
            if (index % 3 == 0) {
                displayLocationInMatrix[index] = counter
                counter++
            }
        }
    }

    private fun getDisplayHolder() {
        //test logic
        for (i in 9 downTo displayLocationInMatrix.count { it > 0 } + 1) {
            displayLocationInHolder.push(i)
        }
    }

    override fun toString(): String {
        var s = displayLocationInMatrix.toString()
        s += displayLocationInHolder.toString().reversed()
        return s
    }




}