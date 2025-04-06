package ru.alexp0111.flexypixel.ui.test.screen

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


@Composable
internal fun DebugCardsScreen() {
    Column(Modifier.fillMaxSize()) {
        MediumTextField("Configuration", Modifier.padding(start = 32.dp))
        DebugConfigCard()
        MediumTextField("Pixels", Modifier.padding(start = 32.dp))
        SendPixelCard()
        MediumTextField("Frames", Modifier.padding(start = 32.dp))
        SendFramesCard()
        MediumTextField("Media", Modifier.padding(start = 32.dp))
        SendMediaCard()
    }
}

@Composable
private fun DebugConfigCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        NeoSlot(modifier = Modifier.weight(1f)) {
            NumberInputText("Total panels")
        }
        NeoImageButton(
            imageId = R.drawable.ic_done,
            modifier = Modifier.size(64.dp),
        )
    }
}

@Composable
private fun SendPixelCard() {
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
                NumberInputText("R")
            }
            NeoSlot(modifier = Modifier.weight(1f)) {
                NumberInputText("G")
            }
            NeoSlot(modifier = Modifier.weight(1f)) {
                NumberInputText("B")
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
                NumberInputText("Panel")
            }
            NeoSlot(modifier = Modifier.weight(1f)) {
                NumberInputText("Pixel")
            }
            NeoImageButton(
                imageId = R.drawable.ic_done,
                modifier = Modifier
                    .height(64.dp)
                    .weight(1f),
            )
        }
    }
}

@Composable
private fun SendFramesCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        NeoSlot(modifier = Modifier.weight(1f)) {
            NumberInputText("Frames")
        }
        NeoSlot(modifier = Modifier.weight(1f)) {
            NumberInputText("Delay")
        }
        NeoImageButton(
            imageId = R.drawable.ic_done,
            modifier = Modifier.size(64.dp),
        )
    }
}

@Composable
private fun SendMediaCard() {
    NeoImageButton(
        imageId = R.drawable.ic_add_file,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    )
}

@Composable
private fun NumberInputText(
    hint: String,
    onValueChanged: (String) -> Unit = {},
    initialText: String = "",
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
        Box(Modifier.fillMaxSize().background(beigeStandard)) {
            DebugCardsScreen()
        }
    }
}