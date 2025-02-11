package ru.alexp0111.core_ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.alexp0111.core_ui.R
import ru.alexp0111.core_ui.common.text.MediumTextField
import ru.alexp0111.core_ui.theme.AppTheme

@Composable
fun NeoButton(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    NeoCard(
        modifier.fillMaxWidth().height(80.dp),
        onClick,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            MediumTextField(title)
        }
    }
}

@Composable
fun NeoImageButton(
    imageId: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    NeoCard(
        modifier.fillMaxWidth().height(80.dp),
        onClick,
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(painterResource(imageId), contentDescription = null)
        }
    }
}

@Preview
@Composable
private fun NeoButtonPreview() {
    AppTheme {
        NeoButton("NeoButton")
    }
}

@Preview
@Composable
private fun NeoImageButtonPreview() {
    AppTheme {
        NeoImageButton(R.drawable.circle_cross)
    }
}