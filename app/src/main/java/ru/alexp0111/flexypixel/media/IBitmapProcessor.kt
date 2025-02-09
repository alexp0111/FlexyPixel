package ru.alexp0111.flexypixel.media

import android.graphics.Bitmap
import ru.alexp0111.flexypixel.data.model.PanelMetaData

interface IBitmapProcessor {
    fun generateBitmapForSegment(panelMetaDataSet: Set<PanelMetaData>, cardSize: Int): Bitmap
    fun convertPixelStringMatrixToBitmap(pixels: Array<String>): Bitmap
}