package ru.alexp0111.flexypixel.ui

import android.graphics.Color

data class DrawingColor(var r: Int = 0, var g: Int = 0, var b: Int = 0) {

    private val ratio = 28
    fun toColor() = Color.rgb(r * ratio, g * ratio, b * ratio)

}