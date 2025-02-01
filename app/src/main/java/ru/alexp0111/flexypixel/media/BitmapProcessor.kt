package ru.alexp0111.flexypixel.media

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.graphics.set
import ru.alexp0111.core.CommonSizeConstants
import ru.alexp0111.core.annotations.FlexyPixelScalable
import ru.alexp0111.core.matrix.MatrixConverter
import ru.alexp0111.flexypixel.data.DrawingColor
import ru.alexp0111.flexypixel.data.model.FrameCycle
import ru.alexp0111.flexypixel.data.model.Panel
import ru.alexp0111.flexypixel.data.model.PanelConfiguration
import ru.alexp0111.flexypixel.data.model.PanelMetaData
import ru.alexp0111.flexypixel.ui.EMPTY_CELL
import javax.inject.Inject
import kotlin.math.round
import kotlin.math.sqrt

class BitmapProcessor @Inject constructor() : IBitmapProcessor {

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

    @FlexyPixelScalable
    override fun generateBitmapForSegment(panelsOrderList: List<Int>, cardSize: Int): Bitmap {
        val panelToOffsetRatio = 4
        val scalingSize = panelToOffsetRatio * CommonSizeConstants.PANELS_MATRIX_SIDE + (CommonSizeConstants.PANELS_MATRIX_SIDE + 1)

        val trimmedBitmapSize = (cardSize - (cardSize % scalingSize))
        val scaledPixelSize = (trimmedBitmapSize / scalingSize).toFloat()
        val squareSize = scaledPixelSize * panelToOffsetRatio

        val bitmap = Bitmap.createBitmap(trimmedBitmapSize, trimmedBitmapSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply { color = Color.BLACK }

        for (i in 0 until CommonSizeConstants.PANELS_MATRIX_SIDE) {
            for (j in 0 until CommonSizeConstants.PANELS_MATRIX_SIDE) {
                val panelIndex = MatrixConverter.XYtoIndex(j, i, CommonSizeConstants.PANELS_MATRIX_SIDE)
                if (panelsOrderList[panelIndex] == EMPTY_CELL) continue

                val x = (j * (squareSize + scaledPixelSize) + scaledPixelSize)
                val y = (i * (squareSize + scaledPixelSize) + scaledPixelSize)

                canvas.drawRoundRect(x, y, x + squareSize, y + squareSize, scaledPixelSize, scaledPixelSize, paint)
            }
        }

        return bitmap
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

    override fun convertPixelStringMatrixToBitmap(pixels: Array<String>): Bitmap {
        return Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888).apply {
            pixels.forEachIndexed { index, str ->
                set(index % 8, index / 8, DrawingColor.getFromString(str).toColor())
            }
        }
    }
}