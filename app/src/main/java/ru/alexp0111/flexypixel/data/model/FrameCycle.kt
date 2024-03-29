package ru.alexp0111.flexypixel.data.model

data class FrameCycle(
    private val configuration: PanelConfiguration,
    private val framesAmount: Int,
    private val frames: List<Frame>,
)