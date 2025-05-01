package ru.alexp0111.flexypixel.media

import android.graphics.Bitmap
import ru.alexp0111.core.model.FrameCycle
import ru.alexp0111.core.model.PanelMetaData
import ru.alexp0111.model.MediaFileType

internal interface IBitmapProcessor {
    fun generateBitmapForSegment(panelMetaDataSet: Set<PanelMetaData>, cardSize: Int): Bitmap
    fun convertPanelPixelsToPanelBimap(pixels: Array<String>): Bitmap
    fun convertBitmapToPixelStringMatrix(bitmap: Bitmap): Array<Array<String>>
    fun applyBitmapsToFrameCycle(frameCycle: FrameCycle, type: MediaFileType, bitmaps: List<Bitmap>)
}