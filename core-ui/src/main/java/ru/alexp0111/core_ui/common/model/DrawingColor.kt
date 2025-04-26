package ru.alexp0111.core_ui.common.model

import android.graphics.Color

data class DrawingColor(var r: Int = 0, var g: Int = 0, var b: Int = 0) {

    fun toColor() = Color.rgb(r * RATIO, g * RATIO, b * RATIO)
    fun asString() = r.toString() + g.toString() + b.toString()

    companion object {
        private const val RATIO = 28
        fun getFromString(stringRepresentation: String): DrawingColor {
            return DrawingColor(
                r = stringRepresentation[0].digitToInt(),
                g = stringRepresentation[1].digitToInt(),
                b = stringRepresentation[2].digitToInt(),
            )
        }
    }

}