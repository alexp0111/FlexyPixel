package ru.alexp0111.flexypixel.data.model

data class FrameCycle(
    var configuration: PanelConfiguration,
    var framesAmount: Int,
    var interframeDelay: Int,
    var frames: MutableList<Frame>
) {
    companion object {
        private const val DEFAULT_FRAMES_AMOUNT = 1
        private const val DEFAULT_INTERFRAME_DELAY = 1000

        fun getNewInstance(): FrameCycle {
            return FrameCycle(
                configuration = PanelConfiguration(mutableListOf()),
                framesAmount = DEFAULT_FRAMES_AMOUNT,
                interframeDelay = DEFAULT_INTERFRAME_DELAY,
                frames = mutableListOf(Frame(mutableListOf()))
            )
        }
    }
}