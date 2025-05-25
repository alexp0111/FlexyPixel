package ru.alexp0111.flexypixel.ui.savedScheme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import ru.alexp0111.core_ui.common.NeoCard
import ru.alexp0111.core_ui.common.shimmer.ShimmeringItem
import ru.alexp0111.core_ui.common.text.LargeTextField
import ru.alexp0111.core_ui.common.text.MediumTextField
import ru.alexp0111.core_ui.common.text.SmallTextField
import ru.alexp0111.core_ui.theme.AppTheme
import ru.alexp0111.flexypixel.ui.savedScheme.model.SavedSchemeIntent
import ru.alexp0111.flexypixel.ui.savedScheme.model.SavedSchemeUiModel
import ru.alexp0111.flexypixel.ui.savedScheme.model.SavedSchemeUiState

@Composable
internal fun SavedSchemeScreen(viewModel: SavedSchemesViewModel) {
    val uiState = viewModel.uiState.collectAsState()
    val intentHandler by rememberUpdatedState(viewModel::sendIntent)

    SavedSchemeScreenContent(uiState, intentHandler)
}

@Composable
private fun SavedSchemeScreenContent(
    uiState: State<SavedSchemeUiState>,
    intentHandler: (SavedSchemeIntent) -> Unit = {},
) {
    Scaffold(
        topBar = {
            Column(Modifier.padding(start = 32.dp, end = 32.dp, top = 32.dp)) {
                LargeTextField("SAVED")
                MediumTextField("schemes")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                if (uiState.value.isLoading) {
                    items(10) {
                        SavedSchemeShimmeringItem()
                    }
                } else {
                    items(uiState.value.schemeItems) {
                        SavedSchemeItem(it, intentHandler)
                    }
                }
            }
        }
    }
}

@Composable
private fun SavedSchemeItem(
    model: SavedSchemeUiModel,
    intentHandler: (SavedSchemeIntent) -> Unit,
) {
    NeoCard(
        modifier = Modifier
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .height(160.dp),
        onClick = {
            val clickIntent = SavedSchemeIntent.SchemeItemClicked(
                model.uuid,
                model.preview.toString(),
                model.createdAt
            )
            intentHandler(clickIntent)
        },
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 30.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            model.preview?.let {
                AsyncImage(
                    modifier = Modifier.size(90.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it)
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp),
                verticalArrangement = Arrangement.Center
            ) {
                MediumTextField(model.title, maxLines = 2, weight = FontWeight.Bold)
                SmallTextField(model.createdAt)
            }
        }
    }
}

@Composable
private fun SavedSchemeShimmeringItem() {
    Box(
        modifier = Modifier
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .height(160.dp),
    ) {
        ShimmeringItem(cornerRadius = 24.dp)
    }
}

@Preview
@Composable
private fun SavedSchemeScreenContentPreview() {
    AppTheme {
        SavedSchemeScreenContent(
            uiState = remember {
                mutableStateOf(
                    SavedSchemeUiState(
                        schemeItems = listOf(
                            SavedSchemeUiModel(
                                uuid = "",
                                title = "First scheme and one more words or a few more",
                                createdAt = "12.04.2025",
                                preview = null,
                            ),
                            SavedSchemeUiModel(
                                uuid = "",
                                title = "Second scheme",
                                createdAt = "15.04.2025",
                                preview = null,
                            ),
                            SavedSchemeUiModel(
                                uuid = "",
                                title = "Third scheme",
                                createdAt = "22.04.2025",
                                preview = null,
                            ),
                        )
                    )
                )
            }
        )
    }
}

@Preview
@Composable
private fun SavedSchemeScreenContentShimmerPreview() {
    AppTheme {
        SavedSchemeScreenContent(
            uiState = remember { mutableStateOf(SavedSchemeUiState(isLoading = true)) }
        )
    }
}