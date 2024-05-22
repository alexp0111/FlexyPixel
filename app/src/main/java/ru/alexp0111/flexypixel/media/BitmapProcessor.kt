package ru.alexp0111.flexypixel.media

import android.graphics.Bitmap
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.graphics.set
import ru.alexp0111.flexypixel.data.DrawingColor
import ru.alexp0111.flexypixel.data.model.FrameCycle
import ru.alexp0111.flexypixel.data.model.Panel
import ru.alexp0111.flexypixel.data.model.PanelConfiguration
import ru.alexp0111.flexypixel.data.model.PanelMetaData
import kotlin.math.round
import kotlin.math.sqrt

object BitmapProcessor {
    fun convertBitmapToPixelStringMatrix(bitmap: Bitmap): Array<Array<String>> {
        val pixelMatrix = Array(bitmap.height) { Array(bitmap.width) { "000" } }
        for (row in 0 until bitmap.height) {
            for (column in 0 until bitmap.width) {
                val pixel = bitmap.getPixel(column, row)
                val rChannel = round(pixel.red * 9 / 255f).toInt().toString()
                val gChannel = round(pixel.green * 9 / 255f).toInt().toString()
                val bChannel = round(pixel.blue * 9 / 255f).toInt().toString()
                pixelMatrix[row][column] = rChannel + gChannel + bChannel
            }
        }
        return pixelMatrix
    }

    /**
     * TODO: Need to be carefully tested!!!
     * */
    fun applyBitmapToFrame(
        frameCycle: FrameCycle,
        frameIndex: Int,
        goalPanels: List<PanelMetaData>,
        bitmapAsMatrix: Array<Array<String>>,
    ) {
        val startPanel = goalPanels.sortedWith(
            compareBy(
                { it.absoluteX },
                { it.absoluteY },
            )
        ).first()
        val config = frameCycle.configuration

        for (row in startPanel.absoluteY until PanelConfiguration.MAX_SIZE) {
            for (colum in startPanel.absoluteX until PanelConfiguration.MAX_SIZE) {
                val panel = config.getPanelMetaDataByCoordinates(colum, row) ?: continue

                val bitmapMatrixRowSegment = row - startPanel.absoluteY
                val bitmapMatrixColumnSegment = colum - startPanel.absoluteX

                val bitmapFragmentAsPanel = getBitmapSegment(
                    bitmapAsMatrix,
                    bitmapMatrixRowSegment,
                    bitmapMatrixColumnSegment,
                    panel.type,
                )

                frameCycle.frames[frameIndex].panels[panel.order] = Panel(bitmapFragmentAsPanel)
            }
        }
    }

    private fun getBitmapSegment(
        bitmapAsMatrix: Array<Array<String>>,
        bitmapMatrixRowSegment: Int,
        bitmapMatrixColumnSegment: Int,
        segmentSize: Int,
    ): Array<String> {
        val rowPosition = bitmapMatrixRowSegment * 8
        val columnPosition = bitmapMatrixColumnSegment * 8
        val segmentSideSize = sqrt(segmentSize.toFloat()).toInt()

        return mutableListOf<String>().apply {
            for (row in rowPosition until (rowPosition + segmentSideSize)) {
                for (column in columnPosition until (columnPosition + segmentSideSize)) {
                    add(bitmapAsMatrix[row][column])
                }
            }
        }.toTypedArray()
    }

    fun convertPixelStringMatrixToBitmap(pixels: Array<String>): Bitmap {
        return Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888) .apply {
            pixels.forEachIndexed { index, str ->
                set(index % 8, index / 8, DrawingColor.getFromString(str).toColor())
            }
        }
    }
}