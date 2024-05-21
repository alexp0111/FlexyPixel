package ru.alexp0111.flexypixel.data.model

data class Frame(
    val configuration: PanelConfiguration,
    var panels: MutableList<Panel>,
)