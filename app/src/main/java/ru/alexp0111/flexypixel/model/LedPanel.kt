package ru.alexp0111.flexypixel.model

interface LedPanel {
    val order: Int
    val pixels: IntArray

    interface Properties {
        val NUMBER_OF_PIXELS: Int
        val SIDE_LENGTH: Int
    }
}

data class LedPanel64(
    override val order: Int,
    override val pixels: IntArray = IntArray(NUMBER_OF_PIXELS) { 0 },
) : LedPanel {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as LedPanel64
        return pixels.contentEquals(other.pixels)
    }

    override fun hashCode(): Int {
        return pixels.contentHashCode()
    }

    companion object : LedPanel.Properties {
        override val NUMBER_OF_PIXELS = 64
        override val SIDE_LENGTH = 8
    }
}

data class Panel256(
    override val order: Int,
    override val pixels: IntArray = IntArray(NUMBER_OF_PIXELS) { 0 },
) : LedPanel {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as LedPanel64
        return pixels.contentEquals(other.pixels)
    }

    override fun hashCode(): Int {
        return pixels.contentHashCode()
    }

    companion object : LedPanel.Properties {
        override val NUMBER_OF_PIXELS = 256
        override val SIDE_LENGTH = 16
    }
}