package ru.alexp0111.flexypixel.data.model

data class PanelConfiguration(
    private val configuration: Array<String> = Array(MAX_SIZE) { "000" },
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PanelConfiguration

        return configuration.contentEquals(other.configuration)
    }

    override fun hashCode(): Int {
        return configuration.contentHashCode()
    }

    companion object{
        const val MAX_SIZE = 9
    }
}