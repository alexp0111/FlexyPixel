package ru.alexp0111.flexypixel.data.model

data class FrameCycle(
    var configuration: PanelConfiguration,
    var framesAmount: Int,
    var interframeDelay: Int,
    var frames: MutableList<Frame>,
)