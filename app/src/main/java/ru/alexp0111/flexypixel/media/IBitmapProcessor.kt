package ru.alexp0111.flexypixel.media

import android.graphics.Bitmap
import ru.alexp0111.flexypixel.data.model.FrameCycle
import ru.alexp0111.flexypixel.data.model.PanelMetaData

interface IBitmapProcessor {
    fun generateBitmapForSegment(panelMetaDataSet: Set<PanelMetaData>, cardSize: Int): Bitmap
    fun convertPanelPixelsToPanelBimap(pixels: Array<String>): Bitmap
    fun convertBitmapToPixelStringMatrix(bitmap: Bitmap): Array<Array<String>>
    fun applyPixelsToFrameCycle(
        frameCycle: FrameCycle,
        frameIndex: Int = 0,
        pixelMatrix: Array<Array<String>>,
    )
}