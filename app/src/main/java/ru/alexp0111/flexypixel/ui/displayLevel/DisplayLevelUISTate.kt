package ru.alexp0111.flexypixel.ui.displayLevel

import kotlinx.coroutines.flow.StateFlow


const val MAX_DISPLAY_NUMBER = 9

data class DisplayLevelUISTate(
    val displayLocationInMatrix: MutableList<Int> = MutableList<Int>(MAX_DISPLAY_NUMBER) { -1 },
    val displayLocationInHolder: MutableList<Int> = MutableList<Int>(MAX_DISPLAY_NUMBER) { -1 }
)

interface StateHolder {
    val state: StateFlow<DisplayLevelUISTate>
}
