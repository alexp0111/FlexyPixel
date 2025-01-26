package ru.alexp0111.flexypixel.ui.upperAbstractionLevel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.alexp0111.core_ui.common.NeoButton
import ru.alexp0111.core_ui.common.NeoCard
import ru.alexp0111.core_ui.common.divider.VerticalEmptyDivider
import ru.alexp0111.core_ui.common.text.LargeEditTextField
import ru.alexp0111.core_ui.common.text.MediumTextField
import ru.alexp0111.core_ui.theme.AppTheme
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model.SegmentUiState
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model.UpperAbstractionLevelEffect
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model.UpperAbstractionLevelIntent
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model.UpperAbstractionLevelUiState

@Composable
internal fun UpperAbstractionLevelScreen(
    viewModel: UpperAbstractionLevelViewModel,
) {
    val uiState = viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val intentHandler by rememberUpdatedState(viewModel::sendIntent)

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when(it) {
                is UpperAbstractionLevelEffect.ShowSnackBar -> snackBarHostState.showSnackbar(it.message)
            }
        }
    }

    UpperAbstractionLevelScreenContent(uiState, snackBarHostState, intentHandler)
}

@Composable
private fun UpperAbstractionLevelScreenContent(
    uiState: State<UpperAbstractionLevelUiState>,
    snackBarHostState: SnackbarHostState,
    intentHandler: (UpperAbstractionLevelIntent) -> Unit = {},
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            ) {
                LargeEditTextField(
                    initialText = uiState.value.title,
                    hint = "Title",
                ) {
                    intentHandler(UpperAbstractionLevelIntent.TitleChanged(it))
                }
                VerticalEmptyDivider(16.dp)
                MediumTextField("tap to change")
            }
        },
        bottomBar = {
            NeoButton(
                modifier = Modifier.padding(32.dp),
                title = "Save"
            ) {
                intentHandler(UpperAbstractionLevelIntent.Save)
            }
        }
    ) { padding ->
        Box(
            Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            UpperAbstractionLevelScreenSegmentMatrix(
                segmentMatrixState = uiState.value.segmentMatrix,
                onCardClicked = { x, y ->
                    intentHandler(UpperAbstractionLevelIntent.CardClicked(x, y))
                }
            )
        }
    }
}

@Composable
private fun UpperAbstractionLevelScreenSegmentMatrix(
    segmentMatrixState: List<List<SegmentUiState>>,
    onCardClicked: (Int, Int) -> Unit,
) {
    Column {
        segmentMatrixState.forEachIndexed { y, _ ->
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                segmentMatrixState[y].forEachIndexed { x, _ ->
                    NeoCard(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(8.dp),
                        onClick = { onCardClicked(x, y) }
                    ) {
                        // TODO: Implement bitmap
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun UpperAbstractionLevelScreenContentPreview() {
    AppTheme {
        UpperAbstractionLevelScreenContent(
            uiState = remember {
                mutableStateOf(
                    UpperAbstractionLevelUiState()
                )
            },
            snackBarHostState = SnackbarHostState()
        )
    }
}