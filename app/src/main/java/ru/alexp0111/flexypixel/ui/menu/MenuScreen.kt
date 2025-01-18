package ru.alexp0111.flexypixel.ui.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.alexp0111.flexypixel.ui.theme.AppTheme

@Composable
fun MenuScreen() {
    Scaffold { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("TODO: Menu Screen")
        }
    }
}

@Preview
@Composable
fun MenuScreenPreview() {
    AppTheme {
        MenuScreen()
    }
}