package ru.alexp0111.flexypixel.data.model

data class PanelConfiguration(
    var listOfMetaData: MutableSet<PanelMetaData>,
) {

    fun getPanelMetaDataByCoordinates(x: Int, y: Int): PanelMetaData? {
        return listOfMetaData.find { it.absoluteX == x && it.absoluteY == y }
    }

    companion object {
        const val MAX_SIZE = 9
    }
}