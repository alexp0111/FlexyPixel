package ru.alexp0111.flexypixel.media

import android.graphics.Bitmap

interface IBitmapProcessor {
    fun generateBitmapForSegment(panelsOrderList: List<Int>, cardSize: Int): Bitmap
    fun convertPixelStringMatrixToBitmap(pixels: Array<String>): Bitmap
}