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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import ru.alexp0111.core_ui.R
import ru.alexp0111.core_ui.common.NeoImageButton
import ru.alexp0111.core_ui.common.NeoSlot
import ru.alexp0111.core_ui.common.text.MediumEditTextField
import ru.alexp0111.core_ui.common.text.SmallTextField
import ru.alexp0111.core_ui.theme.AppTheme
import ru.alexp0111.core_ui.theme.beigeDark
import ru.alexp0111.core_ui.theme.beigeStandard
import ru.alexp0111.core_ui.theme.black


@Composable
internal fun TerminalScreen(messages: List<Pair<String, Color>>) {
    Column {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages.size) { index ->
                SmallTextField(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    text = messages[index].first,
                    color = messages[index].second
                )
            }
        }
        InputMessageRow()
    }
}

@Composable
private fun InputMessageRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var height by remember { mutableStateOf(IntSize.Zero) }
        NeoSlot(
            modifier = Modifier
                .weight(1f)
                .onGloballyPositioned {
                    height = it.size
                }
        ) {
            MediumEditTextField(
                hint = "JSON message",
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                indicatorColor = beigeStandard,
                onValueChanged = { },
            )
        }
        NeoImageButton(
            imageId = R.drawable.ic_send,
            scale = 0.8f,
            modifier = Modifier
                .width(64.dp)
                .height(with(LocalDensity.current) { height.height.toDp() }),
        )
    }
}

@Preview
@Composable
private fun TerminalScreenPreview() {
    AppTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(beigeStandard)
        ) {
            TerminalScreen(getPreviewList())
        }
    }
}

private fun getPreviewList(): List<Pair<String, Color>> =
    listOf(
        Pair("CONF:5,6,7,8", beigeDark),
        Pair("SUCCES", black),
        Pair("VERY LONG 123, 345, 34,5 12,4 1,4 ,1 4,23 2,2,5 ,34 5, 34, ,34 6, ,6 3 6,43 6,3 5, 3,24 ,4", beigeDark),
        Pair("CONF:5,6,7,8", beigeDark),
        Pair("SUCCES", black),
        Pair("VERY LONG 123, 345, 34,5 12,4 1,4 ,1 4,23 2,2,5 ,34 5, 34, ,34 6, ,6 3 6,43 6,3 5, 3,24 ,4", beigeDark),
        Pair("CONF:5,6,7,8", beigeDark),
        Pair("SUCCES", black),
        Pair("VERY LONG 123, 345, 34,5 12,4 1,4 ,1 4,23 2,2,5 ,34 5, 34, ,34 6, ,6 3 6,43 6,3 5, 3,24 ,4", beigeDark),
        Pair("CONF:5,6,7,8", beigeDark),
        Pair("SUCCES", black),
        Pair("VERY LONG 123, 345, 34,5 12,4 1,4 ,1 4,23 2,2,5 ,34 5, 34, ,34 6, ,6 3 6,43 6,3 5, 3,24 ,4", beigeDark),
        Pair("CONF:5,6,7,8", beigeDark),
        Pair("SUCCES", black),
        Pair("VERY LONG 123, 345, 34,5 12,4 1,4 ,1 4,23 2,2,5 ,34 5, 34, ,34 6, ,6 3 6,43 6,3 5, 3,24 ,4", beigeDark),
        Pair("CONF:5,6,7,8", beigeDark),
        Pair("SUCCES", black),
        Pair("VERY LONG 123, 345, 34,5 12,4 1,4 ,1 4,23 2,2,5 ,34 5, 34, ,34 6, ,6 3 6,43 6,3 5, 3,24 ,4", beigeDark),
    )