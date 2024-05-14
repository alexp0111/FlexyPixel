package ru.alexp0111.flexypixel.ui.drawing

import android.graphics.Color


data class DrawingFragmentUIState(
    val pixelPanel: ArrayList<DrawingColor>,
    val palette: ArrayList<DrawingColor>,
    val chosenColor: DrawingColor
)

data class DrawingColor(var r: Int, var g: Int, var b: Int) {

    private val ratio = 28
    fun toColor() = Color.rgb(r * ratio, g * ratio, b * ratio)
    fun fromHexColor(hex: String) {

    }
}



