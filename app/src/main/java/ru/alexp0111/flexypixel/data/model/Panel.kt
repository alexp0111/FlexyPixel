package ru.alexp0111.flexypixel.data.model

data class Panel(
    private var order: Int = 0,
    private val type: Int = TYPE_64,
    private val pixels: Array<String> = Array(TYPE_64) { "000" },
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Panel

        if (order != other.order) return false
        return pixels.contentEquals(other.pixels)
    }

    override fun hashCode(): Int {
        var result = order
        result = 31 * result + pixels.contentHashCode()
        return result
    }

    companion object {
        const val TYPE_64 = 64
        const val TYPE_256 = 256
    }
}