package ru.alexp0111.flexypixel.ui.test.screen

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.alexp0111.core_ui.R
import ru.alexp0111.core_ui.common.NeoImageButton
import ru.alexp0111.core_ui.common.NeoSlot
import ru.alexp0111.core_ui.common.text.MediumEditTextField
import ru.alexp0111.core_ui.common.text.MediumTextField
import ru.alexp0111.core_ui.theme.AppTheme
import ru.alexp0111.core_ui.theme.beigeStandard
import ru.alexp0111.flexypixel.ui.test.model.TestFramesModel
import ru.alexp0111.flexypixel.ui.test.model.TestIntent
import ru.alexp0111.flexypixel.ui.test.model.TestPixelModel
import ru.alexp0111.flexypixel.ui.test.model.TestUiState


@Composable
internal fun DebugCardsScreen(
    uiState: State<TestUiState>,
    intentHandler: (TestIntent) -> Unit = {}
) {
    Column(Modifier.fillMaxSize()) {
        MediumTextField("Configuration", Modifier.padding(start = 32.dp))
        DebugConfigCard(uiState, intentHandler)
        MediumTextField("Pixels", Modifier.padding(start = 32.dp))
        SendPixelCard(uiState, intentHandler)
        MediumTextField("Frames", Modifier.padding(start = 32.dp))
        SendFramesCard(uiState, intentHandler)
        MediumTextField("Media", Modifier.padding(start = 32.dp))
        SendMediaCard(intentHandler)
    }
}

@Composable
private fun DebugConfigCard(
    uiState: State<TestUiState>,
    intentHandler: (TestIntent) -> Unit = {}
) {
    var totalPanels by remember { mutableStateOf(uiState.value.totalAmountOfPanels?.toString() ?: "") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        NeoSlot(modifier = Modifier.weight(1f)) {
            NumberInputText(totalPanels, "Total panels") { totalPanels = it }
        }
        NeoImageButton(
            imageId = R.drawable.ic_done,
            modifier = Modifier.size(64.dp),
            scale = 0.8f,
            onClick = { intentHandler(TestIntent.SendConfig(totalPanels.toIntOrNull() ?: 0)) }
        )
    }
}

@Composable
private fun SendPixelCard(
    uiState: State<TestUiState>,
    intentHandler: (TestIntent) -> Unit = {}
) {
    var r by remember { mutableStateOf(uiState.value.testPixel.r?.toString() ?: "") }
    var g by remember { mutableStateOf(uiState.value.testPixel.g?.toString() ?: "") }
    var b by remember { mutableStateOf(uiState.value.testPixel.b?.toString() ?: "") }

    var panel by remember { mutableStateOf(uiState.value.testPixel.panel?.toString() ?: "") }
    var pixel by remember { mutableStateOf(uiState.value.testPixel.pixel?.toString() ?: "") }

    fun getTestPixelModel() = TestPixelModel(
        r = r.toIntOrNull() ?: 0,
        g = g.toIntOrNull() ?: 0,
        b = b.toIntOrNull() ?: 0,
        panel = panel.toIntOrNull() ?: 0,
        pixel = pixel.toIntOrNull() ?: 0,
    )

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NeoSlot(modifier = Modifier.weight(1f)) {
                NumberInputText(r, "R") { r = it }
            }
            NeoSlot(modifier = Modifier.weight(1f)) {
                NumberInputText(g, "G") { g = it }
            }
            NeoSlot(modifier = Modifier.weight(1f)) {
                NumberInputText(b, "B") { b = it }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NeoSlot(modifier = Modifier.weight(1f)) {
                NumberInputText(panel, "Panel") { panel = it }
            }
            NeoSlot(modifier = Modifier.weight(1f)) {
                NumberInputText(pixel, "Pixel") { pixel = it }
            }
            NeoImageButton(
                imageId = R.drawable.ic_done,
                modifier = Modifier
                    .height(64.dp)
                    .weight(1f),
                scale = 0.8f,
                onClick = { intentHandler(TestIntent.SendPixel(getTestPixelModel())) }
            )
        }
    }
}

@Composable
private fun SendFramesCard(
    uiState: State<TestUiState>,
    intentHandler: (TestIntent) -> Unit = {}
) {
    var frames by remember { mutableStateOf(uiState.value.testFrames.framesAmount?.toString() ?: "") }
    var delay by remember { mutableStateOf(uiState.value.testFrames.interframeDelay?.toString() ?: "") }

    fun getTestFramesModel() = TestFramesModel(
        framesAmount = frames.toIntOrNull() ?: 0,
        interframeDelay = delay.toIntOrNull() ?: 0,
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        NeoSlot(modifier = Modifier.weight(1f)) {
            NumberInputText(frames, "Frames") { frames = it }
        }
        NeoSlot(modifier = Modifier.weight(1f)) {
            NumberInputText(delay, "Delay") { delay = it }
        }
        NeoImageButton(
            imageId = R.drawable.ic_done,
            modifier = Modifier.size(64.dp),
            scale = 0.8f,
            onClick = { intentHandler(TestIntent.SendFrames(getTestFramesModel())) }
        )
    }
}

@Composable
private fun SendMediaCard(
    intentHandler: (TestIntent) -> Unit = {}
) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        it.data?.data?.let { uri ->
            intentHandler(TestIntent.PickMedia(uri))
        }
    }
    val mediaIntent = remember {
        Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "gif/*"))
        }
    }
    NeoImageButton(
        imageId = R.drawable.ic_add_file,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        onClick = { launcher.launch(mediaIntent) }
    )
}

@Composable
private fun NumberInputText(
    initialText: String = "",
    hint: String = "",
    onValueChanged: (String) -> Unit = {},
) {
    MediumEditTextField(
        hint = hint,
        initialText = initialText,
        modifier = Modifier.fillMaxWidth(),
        align = TextAlign.Center,
        inputType = KeyboardType.Number,
        indicatorColor = beigeStandard,
        onValueChanged = onValueChanged,
    )
}

@Preview
@Composable
private fun DebugCardsScreenPreview() {
    AppTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(beigeStandard)
        ) {
            val state = remember { mutableStateOf(getPreviewTestState()) }
            DebugCardsScreen(state)
        }
    }
}