package ru.alexp0111.flexypixel.data.model

data class PanelMetaData(
    var order: Int = 0,
    val type: Int = TYPE_64,
    var absoluteX: Int,
    var absoluteY: Int,
    var rotation: Int
) {
    companion object {
        const val TYPE_64 = 64
        const val TYPE_256 = 256
    }
}