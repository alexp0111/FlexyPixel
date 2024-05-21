package ru.alexp0111.flexypixel.data.model

data class Panel(
    private val pixels: Array<String> = Array(PanelMetaData.TYPE_64) { "000" },
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Panel

        return pixels.contentEquals(other.pixels)
    }

    override fun hashCode(): Int {
        return pixels.contentHashCode()
    }

}