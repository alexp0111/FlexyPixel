package ru.alexp0111.flexypixel.ui.displayLevel.model

internal sealed interface DisplayLevelIntent {
    data class InitSegment(val segmentNumber: Int): DisplayLevelIntent

    data object DeletePanels: DisplayLevelIntent
    data object DownloadFileToPanels: DisplayLevelIntent
    data object GoToPanel: DisplayLevelIntent

    data class Click(val clickIntent: DisplayLevelClick): DisplayLevelIntent
    data class DragAndDrop(val dndIntent: DisplayLevelDragAndDrop): DisplayLevelIntent
}

internal sealed interface DisplayLevelClick {
    data class Select(val panel: PanelUiModel): DisplayLevelClick
    data class UnSelect(val panel: PanelUiModel): DisplayLevelClick
}

internal sealed interface DisplayLevelDragAndDrop {
    data class Up(val panel: PanelUiModel): DisplayLevelDragAndDrop
    data class Down(val dropX: Int, val dropY: Int): DisplayLevelDragAndDrop
    data object Fail: DisplayLevelDragAndDrop
}