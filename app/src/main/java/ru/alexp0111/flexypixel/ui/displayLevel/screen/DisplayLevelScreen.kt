package ru.alexp0111.flexypixel.ui.displayLevel.screen

import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.alexp0111.core.CommonSizeConstants
import ru.alexp0111.core_ui.common.NeoImageButton
import ru.alexp0111.core_ui.common.NeoSlot
import ru.alexp0111.core_ui.common.model.Coordinate
import ru.alexp0111.core_ui.common.text.LargeTextField
import ru.alexp0111.core_ui.common.text.MediumTextField
import ru.alexp0111.core_ui.theme.AppTheme
import ru.alexp0111.core_ui.util.conditional
import ru.alexp0111.flexypixel.R
import ru.alexp0111.flexypixel.data.model.PanelStatus
import ru.alexp0111.flexypixel.ui.displayLevel.DisplayLevelViewModel
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelDragAndDrop.Down
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelDragAndDrop.Fail
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelDragAndDrop.Up
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelIntent
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelUiState
import ru.alexp0111.flexypixel.ui.displayLevel.model.PanelUiModel
import ru.alexp0111.core_ui.R as coreUiR


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
    val panelSize = remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            Column(Modifier.padding(32.dp)) {
                LargeTextField("CONFIGURE")
                MediumTextField("your display")
            }
        },
        bottomBar = {
            if (uiState.value.isSelectionModeOn) {
                BottomPanelActions(uiState.value.isOnlyOnePanelSelected, intentHandler)
            } else {
                BottomPanelSource(offsetMap, panelSize, intentHandler)
            }
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            DisplayLevelPanelMatrixContent(uiState, offsetMap, panelSize, intentHandler)
        }
    }
}

@Composable
private fun BottomPanelActions(
    isOnlyOnePanelSelected: Boolean,
    intentHandler: (DisplayLevelIntent) -> Unit = {},
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NeoImageButton(
            imageId = coreUiR.drawable.ic_delete,
            modifier = Modifier.width(80.dp),
            onClick = { intentHandler(DisplayLevelIntent.DeletePanels) }
        )
        if (isOnlyOnePanelSelected) {
            NeoImageButton(
                imageId = coreUiR.drawable.ic_edit,
                modifier = Modifier.weight(1f),
                onClick = { intentHandler(DisplayLevelIntent.GoToPanel) }
            )
        }
        NeoImageButton(
            imageId = coreUiR.drawable.ic_add_file,
            modifier = Modifier
                .width(80.dp)
                .conditional(!isOnlyOnePanelSelected) { weight(1f) },
            onClick = { intentHandler(DisplayLevelIntent.DownloadFileToPanels) }
        )
    }
}

@Composable
private fun BottomPanelSource(
    offsetMap: MutableMap<Coordinate, Rect>,
    panelSize: MutableIntState,
    intentHandler: (DisplayLevelIntent) -> Unit = {},
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(180.dp),
        contentAlignment = Alignment.Center
    ) {
        DraggableView(
            onDragStart = { onDragStart(intentHandler, PanelUiModel()) },
            onDragEnd = { offset -> onDragEnd(intentHandler, offsetMap, offset) }
        ) {
            DisplayLevelPanel(panelSize.intValue, PanelUiModel())
        }
    }
}


@Composable
private fun DisplayLevelPanelMatrixContent(
    uiState: State<DisplayLevelUiState>,
    offsetMap: MutableMap<Coordinate, Rect>,
    panelSize: MutableIntState,
    intentHandler: (DisplayLevelIntent) -> Unit = {},
) {
    Column {
        (0 until CommonSizeConstants.PANELS_MATRIX_SIDE).forEach { y ->
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                (0 until CommonSizeConstants.PANELS_MATRIX_SIDE).forEach { x ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(8.dp)
                    ) {
                        DisplayLevelPanelTarget(uiState, offsetMap, panelSize, intentHandler, x, y)
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
    panelSize: MutableIntState,
    intentHandler: (DisplayLevelIntent) -> Unit = {},
    x: Int,
    y: Int,
) {
    val containsPanel = uiState.value.get(x, y)?.sourceY == y && uiState.value.get(x, y)?.sourceX == x
    val currentPanel = uiState.value.get(x, y) ?: PanelUiModel()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { layoutCoordinates ->
                panelSize.intValue = layoutCoordinates.size.height
                layoutCoordinates
                    .boundsInWindow()
                    .let { rect ->
                        offsetMap[Coordinate(x, y)] = rect
                    }
            },
        contentAlignment = Alignment.Center
    ) {
        NeoSlot(
            modifier = Modifier.fillMaxSize(),
            strokeWidth = if (containsPanel) 0.dp else 6.dp,
        ) {
            if (containsPanel) {
                DraggableView(
                    modifier = Modifier.fillMaxSize(),
                    onDragStart = { onDragStart(intentHandler, currentPanel) },
                    onDragEnd = { offset -> onDragEnd(intentHandler, offsetMap, offset) },
                    onClick = { intentHandler(DisplayLevelIntent.Click(currentPanel)) }
                ) {
                    DisplayLevelPanel(panelSize.intValue, currentPanel)
                }
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
private fun DisplayLevelScreenContentEmptyPreview() {
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

@Preview
@Composable
private fun DisplayLevelScreenContentFullPreview() {
    val resources = LocalContext.current.resources
    AppTheme {
        DisplayLevelScreenContent(
            uiState = remember {
                mutableStateOf(
                    DisplayLevelUiState(
                        panels = setOf(
                            PanelUiModel(
                                sourceX = 0,
                                sourceY = 0,
                                order = 4,
                                status = PanelStatus.PLACED_WRONG
                            ),
                            PanelUiModel(
                                sourceX = 1,
                                sourceY = 0,
                                order = 3,
                                status = PanelStatus.SELECTED
                            ),
                            PanelUiModel(
                                sourceX = 1,
                                sourceY = 1,
                                order = 2
                            ),
                            PanelUiModel(
                                bitmap = BitmapFactory.decodeResource(
                                    resources,
                                    R.drawable.cat_8_8
                                ),
                                sourceX = 1,
                                sourceY = 2,
                                order = 1,
                                status = PanelStatus.SELECTED
                            ),
                            PanelUiModel(
                                bitmap = BitmapFactory.decodeResource(
                                    resources,
                                    R.drawable.cat_8_8
                                ),
                                sourceX = 2,
                                sourceY = 2,
                                order = 0
                            ),
                        )
                    )
                )
            },
        )
    }
}
