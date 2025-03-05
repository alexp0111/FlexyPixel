package ru.alexp0111.flexypixel.media

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.graphics.set
import ru.alexp0111.core.CommonSizeConstants
import ru.alexp0111.core.annotations.FlexyPixelScalable
import ru.alexp0111.core_ui.theme.beigeDark
import ru.alexp0111.core_ui.theme.black
import ru.alexp0111.core_ui.theme.red
import ru.alexp0111.flexypixel.data.DrawingColor
import ru.alexp0111.flexypixel.data.model.FrameCycle
import ru.alexp0111.flexypixel.data.model.Panel
import ru.alexp0111.flexypixel.data.model.PanelMetaData
import ru.alexp0111.flexypixel.data.model.PanelStatus
import javax.inject.Inject
import kotlin.math.round
import kotlin.math.sqrt

class BitmapProcessor @Inject constructor() : IBitmapProcessor {

    @FlexyPixelScalable
    override fun generateBitmapForSegment(panelMetaDataSet: Set<PanelMetaData>, cardSize: Int): Bitmap {
        val panelToOffsetRatio = 4
        val scalingSize = panelToOffsetRatio * CommonSizeConstants.PANELS_MATRIX_SIDE + (CommonSizeConstants.PANELS_MATRIX_SIDE + 1)

        val trimmedBitmapSize = (cardSize - (cardSize % scalingSize))
        val scaledPixelSize = (trimmedBitmapSize / scalingSize).toFloat()
        val squareSize = scaledPixelSize * panelToOffsetRatio

        val bitmap = Bitmap.createBitmap(trimmedBitmapSize, trimmedBitmapSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()

        for (panelY in 0 until CommonSizeConstants.PANELS_MATRIX_SIDE) {
            for (panelX in 0 until CommonSizeConstants.PANELS_MATRIX_SIDE) {
                val panel = panelMetaDataSet.find { it.localX == panelX && it.localY == panelY } ?: continue

                paint.color = when(panel.status) {
                    PanelStatus.DEFAULT -> black.toArgb()
                    PanelStatus.SELECTED -> beigeDark.toArgb()
                    PanelStatus.PLACED_WRONG -> red.toArgb()
                }
                val x = (panelX * (squareSize + scaledPixelSize) + scaledPixelSize)
                val y = (panelY * (squareSize + scaledPixelSize) + scaledPixelSize)

                canvas.drawRoundRect(x, y, x + squareSize, y + squareSize, scaledPixelSize, scaledPixelSize, paint)
            }
        }

        return bitmap
    }

    @FlexyPixelScalable
    override fun convertBitmapToPixelStringMatrix(bitmap: Bitmap): Array<Array<String>> {
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
    override fun convertPanelPixelsToPanelBimap(pixels: Array<String>): Bitmap {
        val blankBitmap = Bitmap.createBitmap(
            CommonSizeConstants.PANEL_PIXEL_SIDE,
            CommonSizeConstants.PANEL_PIXEL_SIDE,
            Bitmap.Config.ARGB_8888
        )
        return blankBitmap.apply {
            pixels.forEachIndexed { index, str ->
                set(
                    x = index % CommonSizeConstants.PANEL_PIXEL_SIDE,
                    y = index / CommonSizeConstants.PANEL_PIXEL_SIDE,
                    color = DrawingColor.getFromString(str).toColor(),
                )
            }
        }
    }


    override fun applyPixelsToFrameCycle(
        frameCycle: FrameCycle,
        frameIndex: Int,
        pixelMatrix: Array<Array<String>>,
    ) {
        val config = frameCycle.configuration
        val goalPanels = config.listOfMetaData.filter { it.status == PanelStatus.SELECTED }
        val startPanel = goalPanels.sortedWith(
            compareBy(
                { it.absoluteX },
                { it.absoluteY },
            )
        ).first()

        for (row in startPanel.absoluteY until CommonSizeConstants.PANELS_TOTAL_AMOUNT) {
            for (colum in startPanel.absoluteX until CommonSizeConstants.PANELS_TOTAL_AMOUNT) {
                val panel = config.getPanelMetaDataByCoordinates(colum, row) ?: continue

                val bitmapMatrixRowSegment = row - startPanel.absoluteY
                val bitmapMatrixColumnSegment = colum - startPanel.absoluteX

                val bitmapFragmentAsPanel = getBitmapSegment(
                    pixelMatrix,
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
}