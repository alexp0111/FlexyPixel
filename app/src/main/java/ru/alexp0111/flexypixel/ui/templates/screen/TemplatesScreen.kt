package ru.alexp0111.flexypixel.ui.templates.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import ru.alexp0111.core_ui.common.text.LargeTextField
import ru.alexp0111.core_ui.common.text.MediumTextField
import ru.alexp0111.core_ui.theme.AppTheme
import ru.alexp0111.flexypixel.database.templates.prepared.PreparedTemplatesLoader
import ru.alexp0111.flexypixel.ui.templates.TemplatesViewModel
import ru.alexp0111.flexypixel.ui.templates.model.TemplateItemUiModel
import ru.alexp0111.flexypixel.ui.templates.model.TemplatesIntent
import ru.alexp0111.flexypixel.ui.templates.model.TemplatesUiState

@Composable
internal fun TemplatesScreen(viewModel: TemplatesViewModel) {
    val uiState = viewModel.uiState.collectAsState()
    val intentHandler by rememberUpdatedState(viewModel::sendIntent)

    TemplatesScreenContent(uiState, intentHandler)
}

@Composable
private fun TemplatesScreenContent(
    uiState: State<TemplatesUiState>,
    intentHandler: (TemplatesIntent) -> Unit = {},
) {
    Scaffold(
        topBar = {
            Column(Modifier.padding(32.dp)) {
                LargeTextField("STANDART")
                MediumTextField("presets")
            }
        }
    ) { padding ->
        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(top = 16.dp),
            columns = StaggeredGridCells.Fixed(2),
        ) {
            itemsIndexed(
                items = uiState.value.templates,
                key = { _, item -> item.id },
            ) { _, item ->
                Box(
                    Modifier.padding(vertical = 16.dp).animateItem(),
                    contentAlignment = Alignment.Center,
                ) {
                    TemplateItem(item, intentHandler)
                }
            }
        }
    }
}

@Preview
@Composable
private fun TemplatesScreenContentPreview() {
    AppTheme {
        TemplatesScreenContent(
            uiState = MutableStateFlow(
                TemplatesUiState(
                    PreparedTemplatesLoader().loadDefaultTemplates().map {
                        TemplateItemUiModel(
                            id = 0,
                            enabled = false,
                            pixels = it.pixels,
                            orientations = it.orientations,
                            currentPanel = 0,
                        )
                    }
                )
            ).collectAsState()
        )
    }
}