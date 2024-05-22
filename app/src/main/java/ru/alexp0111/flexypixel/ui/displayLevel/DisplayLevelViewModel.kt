package ru.alexp0111.flexypixel.ui.displayLevel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.alexp0111.flexypixel.data.model.PanelConfiguration
import ru.alexp0111.flexypixel.data.model.PanelMetaData
import ru.alexp0111.flexypixel.ui.EMPTY_CELL
import ru.alexp0111.flexypixel.ui.GlobalStateHandler
import ru.alexp0111.flexypixel.ui.GlobalStateHandlerFactory
import java.util.Stack

@AssistedFactory
interface DisplayLevelViewModelFactory {
    fun create(schemeId: Int? = null): DisplayLevelViewModel
}

class DisplayLevelViewModel @AssistedInject constructor(
    @Assisted private val schemeId: Int?,
    globalStateHandlerFactory: GlobalStateHandlerFactory,
) : ViewModel() {

    private val globalStateHandler =
        GlobalStateHandler.getInstance(globalStateHandlerFactory, schemeId)

    var displayLocationInMatrix =
        MutableStateFlow(MutableList(PanelConfiguration.MAX_SIZE) { EMPTY_CELL })
    var displayLocationInHolder = MutableStateFlow(Stack<Int>())
    var bitmapMap: MutableStateFlow<MutableMap<Int, Bitmap>> = MutableStateFlow(mutableMapOf())

    companion object {
        const val HOLDER_POSITION = -1
        const val DESTINATION_IS_HOLDER = -1
    }

    fun getPanelsConfiguration() {
        viewModelScope.launch(Dispatchers.IO) {
            getDisplayMatrix()
            getDisplayHolder()
            getDisplayImages()
        }
    }

    /*
    * TODO:
    *  1. Approve only last panel movement
    * */

    fun fromHolderToMatrix(destinationPosition: Int) {
        val panelNumber = displayLocationInHolder.value.pop()
        displayLocationInMatrix.value[destinationPosition] =
            panelNumber
    }

    fun fromMatrixToMatrix(ownerPosition: Int, destinationPosition: Int) {
        displayLocationInMatrix.value[destinationPosition] =
            displayLocationInMatrix.value[ownerPosition]
        displayLocationInMatrix.value[ownerPosition] = EMPTY_CELL
    }

    fun fromMatrixToHolder(ownerPosition: Int) {
        val panelNumber = displayLocationInMatrix.value[ownerPosition]
        displayLocationInMatrix.value[ownerPosition] = EMPTY_CELL
        displayLocationInHolder.value.push(panelNumber)
    }

    private fun getDisplayImages(segmentNum: Int = 0) {
        bitmapMap.update {
            globalStateHandler.getPanelsImages(segmentNum)
        }
    }

    private fun getDisplayMatrix(segmentNum: Int = 0) {
        displayLocationInMatrix.update {
            globalStateHandler.getPanelsConfiguration(segmentNum)
        }
    }

    private fun getDisplayHolder() {
        displayLocationInHolder.update {
            val topElement = globalStateHandler.getHolderUpperItem()
            Stack<Int>().apply {
                for (displayNumber in PanelConfiguration.MAX_SIZE - 1 downTo topElement) {
                    push(displayNumber)
                }
            }
        }
    }

    fun sendPanelsConfiguration(segmentNum: Int = 0) {
        val configuration = getPanelsAbsoluteConfiguration(segmentNum)
        viewModelScope.launch(Dispatchers.IO) {
            globalStateHandler.setPanelsConfiguration(configuration)
        }
    }

    private fun getPanelsAbsoluteConfiguration(segmentNum: Int): MutableMap<Int, Pair<Int, Int>> {
        return mutableMapOf<Int, Pair<Int, Int>>().apply {
            for ((index, panel) in displayLocationInMatrix.value.withIndex()) {
                if (panel == EMPTY_CELL) continue
                this[panel] = PanelMetaData.getAbsoluteCoordinates(index, segmentNum)
            }
        }
    }


    //test function
    override fun toString(): String {
        var s = displayLocationInMatrix.value.toString()
        s += displayLocationInHolder.value.reversed().toString()
        return s
    }

}

