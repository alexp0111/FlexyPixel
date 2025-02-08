package ru.alexp0111.flexypixel.ui.displayLevel.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.alexp0111.core_ui.common.model.Coordinate
import ru.alexp0111.core_ui.common.text.LargeTextField
import ru.alexp0111.core_ui.common.text.MediumTextField
import ru.alexp0111.core_ui.theme.AppTheme
import ru.alexp0111.core_ui.theme.beigeDark
import ru.alexp0111.flexypixel.ui.displayLevel.DisplayLevelViewModel
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelDragAndDrop.Down
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelDragAndDrop.Fail
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelDragAndDrop.Up
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelIntent
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelUiState
import ru.alexp0111.flexypixel.ui.displayLevel.model.PanelUiModel


@Composable
internal fun DisplayLevelScreen(
    viewModel: DisplayLevelViewModel,
) {
    val uiState = viewModel.uiState.collectAsState()
    val intentHandler by rememberUpdatedState(viewModel::sendIntent)

    DragAndDropStateHandler {
        DisplayLevelScreenContent(uiState, intentHandler)
    }
}

@Composable
private fun DisplayLevelScreenContent(
    uiState: State<DisplayLevelUiState>,
    intentHandler: (DisplayLevelIntent) -> Unit = {},
) {
    val offsetMap = remember { mutableMapOf<Coordinate, Rect>() }

    Scaffold(
        topBar = {
            Column(Modifier.padding(32.dp)) {
                LargeTextField("CONFIGURE")
                MediumTextField("your display")
            }
        },
        bottomBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                DraggableView(
                    onDragStart = { onDragStart(intentHandler, PanelUiModel()) },
                    onDragEnd = { offset -> onDragEnd(intentHandler, offsetMap, offset) }
                ) {
                    DisplayLevelPanel()
                }
            }
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            DisplayLevelPanelMatrixContent(uiState, offsetMap, intentHandler)
        }
    }
}


@Composable
private fun DisplayLevelPanelMatrixContent(
    uiState: State<DisplayLevelUiState>,
    offsetMap: MutableMap<Coordinate, Rect>,
    intentHandler: (DisplayLevelIntent) -> Unit = {},
) {
    Column {
        uiState.value.panelMatrix.forEachIndexed { y, _ ->
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                uiState.value.panelMatrix[y].forEachIndexed { x, _ ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(8.dp)
                    ) {
                        DisplayLevelPanelTarget(uiState, offsetMap, intentHandler, x, y)
                    }
                }
            }
        }
    }
}

@Composable
private fun DisplayLevelPanelTarget(
    uiState: State<DisplayLevelUiState>,
    offsetMap: MutableMap<Coordinate, Rect>,
    intentHandler: (DisplayLevelIntent) -> Unit = {},
    x: Int,
    y: Int,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(beigeDark)
            .onGloballyPositioned { layoutCoordinates ->
                layoutCoordinates
                    .boundsInWindow()
                    .let { rect ->
                        offsetMap[Coordinate(x, y)] = rect
                    }
            },
        contentAlignment = Alignment.Center
    ) {
        DraggableView(
            onDragStart = { onDragStart(intentHandler, uiState.value.panelMatrix[y][x]) },
            onDragEnd = { offset -> onDragEnd(intentHandler, offsetMap, offset) },
            onClick = { /* TODO: handle onTap & Enter edit mode */ }
        ) {
            if (uiState.value.panelMatrix[y][x].sourceY == y && uiState.value.panelMatrix[y][x].sourceX == x) {
                DisplayLevelPanel()
            }
        }
    }
}

private fun onDragStart(intentHandler: (DisplayLevelIntent) -> Unit, panel: PanelUiModel) {
    intentHandler(DisplayLevelIntent.DragAndDrop(Up(panel)))
}

private fun onDragEnd(
    intentHandler: (DisplayLevelIntent) -> Unit,
    offsetMap: MutableMap<Coordinate, Rect>,
    offset: Offset,
) {
    val targetCoordinate = extractCoordinateByOffset(offsetMap, offset)
    if (targetCoordinate == null) {
        intentHandler(DisplayLevelIntent.DragAndDrop(Fail))
    } else {
        intentHandler(DisplayLevelIntent.DragAndDrop(Down(targetCoordinate.x, targetCoordinate.y)))
    }
}

private fun extractCoordinateByOffset(offsetMap: MutableMap<Coordinate, Rect>, offset: Offset): Coordinate? =
    offsetMap.filter { (_, rect) -> rect.contains(offset) }.toList().firstOrNull()?.first


@Preview
@Composable
private fun DisplayLevelScreenContentPreview() {
    AppTheme {
        DisplayLevelScreenContent(
            uiState = remember {
                mutableStateOf(
                    DisplayLevelUiState()
                )
            },
        )
    }
}
