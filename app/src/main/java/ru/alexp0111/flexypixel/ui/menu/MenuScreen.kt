package ru.alexp0111.flexypixel.ui.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.alexp0111.flexypixel.R
import ru.alexp0111.core_ui.common.LargeTextField
import ru.alexp0111.core_ui.common.MediumTextField
import ru.alexp0111.core_ui.common.NeoCard
import ru.alexp0111.core_ui.common.SmallTextField
import ru.alexp0111.core_ui.theme.AppTheme

@Composable
fun MenuScreen() {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column(Modifier.padding(start = 36.dp, end = 36.dp, top = 36.dp)) {
                LargeTextField("CHOOSE")
                MediumTextField("an option")
            }
            OptionsList()
        }
    }
}

@Composable
private fun OptionsList() {
    Column(
        modifier = Modifier.padding(top = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        MenuCard(
            iconId = R.drawable.plus_file,
            title = "CREATE",
            subtitle = "new scheme"
        )
        MenuCard(
            iconId = R.drawable.user_file,
            title = "CHOOSE",
            subtitle = "saved scheme"
        )
        MenuCard(
            iconId = R.drawable.star_file,
            title = "CHOOSE",
            subtitle = "template"
        )
    }
}

@Composable
private fun MenuCard(
    iconId: Int,
    title: String,
    subtitle: String,
    onClick: () -> Unit = {},
) {
    NeoCard(
        modifier = Modifier
            .padding(horizontal = 36.dp, vertical = 18.dp)
            .height(160.dp),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 30.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier.size(60.dp),
                painter = painterResource(iconId),
                contentDescription = null,
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp),
                verticalArrangement = Arrangement.Center
            ) {
                MediumTextField(title, weight = FontWeight.Bold)
                SmallTextField(subtitle)
            }
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