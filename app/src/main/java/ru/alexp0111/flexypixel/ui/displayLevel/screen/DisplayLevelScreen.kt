package ru.alexp0111.flexypixel.ui.displayLevel.screen

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.alexp0111.core.CommonSizeConstants
import ru.alexp0111.core.model.DrawingColor
import ru.alexp0111.core.model.PanelStatus
import ru.alexp0111.core_ui.common.NeoImageButton
import ru.alexp0111.core_ui.common.NeoSlot
import ru.alexp0111.core.model.Coordinate
import ru.alexp0111.core_ui.common.text.LargeTextField
import ru.alexp0111.core_ui.common.text.MediumTextField
import ru.alexp0111.core_ui.theme.AppTheme
import ru.alexp0111.core_ui.util.conditional
import ru.alexp0111.flexypixel.ui.displayLevel.DisplayLevelViewModel
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelDragAndDrop.Down
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelDragAndDrop.Fail
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelDragAndDrop.Up
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelIntent
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelUiState
import ru.alexp0111.flexypixel.ui.displayLevel.model.PanelUiModel
import ru.alexp0111.core_ui.R as coreUiR

private const val DRAG_AND_DROP_SCALE_RATIO = 1.2f
private const val DRAG_AND_DROP_ALPHA = 0.8f

@Composable
internal fun DisplayLevelScreen(
    viewModel: DisplayLevelViewModel,
) {
    val uiState = viewModel.uiState.collectAsState()
    val intentHandler by rememberUpdatedState(viewModel::sendIntent)

    DragAndDropStateHandler(
        dndObjectDecorator = {
            scaleX = DRAG_AND_DROP_SCALE_RATIO
            scaleY = DRAG_AND_DROP_SCALE_RATIO
            alpha = DRAG_AND_DROP_ALPHA
        }
    ) {
        DisplayLevelScreenContent(uiState, intentHandler)
    }
}

@Composable
private fun DisplayLevelScreenContent(
    uiState: State<DisplayLevelUiState>,
    intentHandler: (DisplayLevelIntent) -> Unit = {},
) {
    Scaffold(
        topBar = {
            Column(Modifier.padding(32.dp)) {
                LargeTextField("CONFIGURE")
                MediumTextField("your display")
            }
        },
        bottomBar = {
            if (uiState.value.isSelectionModeOn) {
                BottomPanelActions(
                    isOnlyOnePanelSelected = uiState.value.isOnlyOnePanelSelected,
                    selectedAreaIsSquare = uiState.value.selectedAreaIsSquare,
                    intentHandler = intentHandler,
                )
            } else {
                BottomPanelSource(uiState, intentHandler)
            }
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            DisplayLevelPanelMatrixContent(uiState, intentHandler)
        }
    }
}

@Composable
private fun BottomPanelActions(
    isOnlyOnePanelSelected: Boolean,
    selectedAreaIsSquare: Boolean,
    intentHandler: (DisplayLevelIntent) -> Unit = {},
) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        it.data?.data?.let { uri ->
            intentHandler(DisplayLevelIntent.DownloadFileToPanels(uri))
        }
    }
    val mediaIntent = remember {
        Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "gif/*"))
        }
    }
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
            modifier = Modifier
                .width(80.dp)
                .conditional(!isOnlyOnePanelSelected && !selectedAreaIsSquare) { weight(1f) },
            onClick = { intentHandler(DisplayLevelIntent.DeletePanels) }
        )
        if (isOnlyOnePanelSelected) {
            NeoImageButton(
                imageId = coreUiR.drawable.ic_edit,
                modifier = Modifier.weight(1f),
                onClick = { intentHandler(DisplayLevelIntent.GoToPanel) }
            )
        }
        if (selectedAreaIsSquare) {
            NeoImageButton(
                imageId = coreUiR.drawable.ic_add_file,
                modifier = Modifier
                    .width(80.dp)
                    .conditional(!isOnlyOnePanelSelected) { weight(1f) },
                onClick = { launcher.launch(mediaIntent) }
            )
        }
    }
}

@Composable
private fun BottomPanelSource(
    uiState: State<DisplayLevelUiState>,
    intentHandler: (DisplayLevelIntent) -> Unit = {},
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(180.dp),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.value.isPanelLimitAchieved) {
            Image(
                painter = painterResource(coreUiR.drawable.ic_done),
                contentDescription = null,
            )
        } else {
            DraggableView(
                onDragStart = { onDragStart(PanelUiModel(), intentHandler) },
                onDragEnd = { targetCoordinate -> onDragEnd(targetCoordinate, intentHandler) }
            ) { panelSize ->
                DisplayLevelPanel(panelSize, PanelUiModel())
            }
        }
    }
}


@Composable
private fun DisplayLevelPanelMatrixContent(
    uiState: State<DisplayLevelUiState>,
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
                        DisplayLevelPanelTarget(uiState, intentHandler, x, y)
                    }
                }
            }
        }
    }
}

@Composable
private fun DisplayLevelPanelTarget(
    uiState: State<DisplayLevelUiState>,
    intentHandler: (DisplayLevelIntent) -> Unit = {},
    x: Int,
    y: Int,
) {
    val currentPanel = uiState.value.get(x, y) ?: PanelUiModel()
    val containsPanel = currentPanel.sourceY == y && currentPanel.sourceX == x

    DragAndDropTarget(
        modifier = Modifier.fillMaxSize(),
        coordinate = Coordinate(x, y),
    ) {
        NeoSlot(
            modifier = Modifier.fillMaxSize(),
            strokeWidth = if (containsPanel) 0.dp else 6.dp,
        ) {
            if (containsPanel) {
                DraggableView(
                    modifier = Modifier.fillMaxSize(),
                    onDragStart = { onDragStart(currentPanel, intentHandler) },
                    onDragEnd = { targetCoordinate -> onDragEnd(targetCoordinate, intentHandler) },
                    onClick = { intentHandler(DisplayLevelIntent.Click(currentPanel)) }
                ) { panelSize ->
                    DisplayLevelPanel(panelSize, currentPanel)
                }
            }
        }
    }
}

private fun onDragStart(panel: PanelUiModel, intentHandler: (DisplayLevelIntent) -> Unit) {
    intentHandler(DisplayLevelIntent.DragAndDrop(Up(panel)))
}

private fun onDragEnd(
    targetCoordinate: Coordinate?,
    intentHandler: (DisplayLevelIntent) -> Unit,
) {
    if (targetCoordinate == null) {
        intentHandler(DisplayLevelIntent.DragAndDrop(Fail))
    } else {
        intentHandler(DisplayLevelIntent.DragAndDrop(Down(targetCoordinate)))
    }
}


@Preview
@Composable
private fun DisplayLevelScreenContentEmptyPreview() {
    AppTheme {
        DisplayLevelScreenContent(
            uiState = remember { mutableStateOf(DisplayLevelUiState(isPanelLimitAchieved = false)) },
        )
    }
}

@Preview
@Composable
private fun DisplayLevelScreenContentPanelLimitAchieved() {
    AppTheme {
        DisplayLevelScreenContent(
            uiState = remember { mutableStateOf(DisplayLevelUiState(isPanelLimitAchieved = true)) },
        )
    }
}

@Preview
@Composable
private fun DisplayLevelScreenContentFullPreview() {
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
                                pixels = Array(64) { listOf("007", "070", "700").random() }
                                    .map { DrawingColor.getFromString(it) },
                                sourceX = 1,
                                sourceY = 2,
                                order = 1,
                                status = PanelStatus.SELECTED
                            ),
                            PanelUiModel(
                                pixels = Array(64) { listOf("007", "070", "700").random() }
                                    .map { DrawingColor.getFromString(it) },
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
