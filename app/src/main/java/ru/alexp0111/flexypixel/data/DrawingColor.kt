package ru.alexp0111.flexypixel.data

import android.graphics.Color

data class DrawingColor(var r: Int = 0, var g: Int = 0, var b: Int = 0) {

    private val ratio = 28
    fun toColor() = Color.rgb(r * ratio, g * ratio, b * ratio)
    fun asString() = r.toString() + g.toString() + b.toString()

    companion object {
        fun getFromString(stringRepresentation: String): DrawingColor {
            return DrawingColor(
                r = stringRepresentation[0].digitToInt(),
                g = stringRepresentation[1].digitToInt(),
                b = stringRepresentation[2].digitToInt(),
            )
        }
    }

}