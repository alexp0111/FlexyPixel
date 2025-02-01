package ru.alexp0111.flexypixel.ui.upperAbstractionLevel

import android.graphics.Bitmap

interface UpperAbstractionLevelGlobalStateHolder {
    fun getSegmentsBitmapImages(size: Int) : List<Bitmap?>
}