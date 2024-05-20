package ru.alexp0111.flexypixel.ui.upperAbstractionLevel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class UpperAbstractionLevelViewModel @Inject constructor() :
    ViewModel() {
    val segmentImagesBitmapList: MutableStateFlow<List<Bitmap?>> =
        MutableStateFlow(MutableList(SEGMENTS_NUMBER) { null })



    companion object {
        const val SEGMENTS_NUMBER = 9
    }

    fun getSegmentsImages(){
        //TODO collect images
    }
}