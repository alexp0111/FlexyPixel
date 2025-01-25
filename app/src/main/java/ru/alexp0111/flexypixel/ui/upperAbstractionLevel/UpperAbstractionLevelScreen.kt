package ru.alexp0111.flexypixel.ui.upperAbstractionLevel

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.alexp0111.core_ui.common.NeoButton
import ru.alexp0111.core_ui.common.NeoCard
import ru.alexp0111.core_ui.common.divider.VerticalEmptyDivider
import ru.alexp0111.core_ui.common.text.LargeEditTextField
import ru.alexp0111.core_ui.common.text.MediumTextField
import ru.alexp0111.core_ui.theme.AppTheme
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model.SegmentUiState
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model.UpperAbstractionLevelIntent
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model.UpperAbstractionLevelUiState

@Composable
internal fun UpperAbstractionLevelScreen(
    viewModel: UpperAbstractionLevelViewModel,
) {
    val uiState = viewModel.uiState.collectAsState()
    val intentHandler by rememberUpdatedState(viewModel::sendIntent)
    UpperAbstractionLevelScreenContent(uiState, intentHandler)
}

@Composable
private fun UpperAbstractionLevelScreenContent(
    uiState: State<UpperAbstractionLevelUiState>,
    intentHandler: (UpperAbstractionLevelIntent) -> Unit = {},
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            ) {
                LargeEditTextField(uiState.value.title) {
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
                Toast.makeText(context, "sus", Toast.LENGTH_SHORT).show()
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
            }
        )
    }
}