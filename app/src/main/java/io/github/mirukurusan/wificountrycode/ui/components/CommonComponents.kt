package io.github.mirukurusan.wificountrycode.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.clipScrollableContainer
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp


@Composable
fun ScrollColumn(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .clipScrollableContainer(Orientation.Horizontal)
            .then(modifier),
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = verticalArrangement,
        content = content
    )
}

@Composable
fun SmallTitle(label: @Composable () -> String) {
    Text(
        text = label(),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface.copy(0.6F),
        modifier = Modifier.padding(top = 16.dp, bottom = 1.dp)
    )
}

@Composable
fun TextContent(
    fontFamily: FontFamily = FontFamily.SansSerif,
    onClick: () -> Unit = {},
    content: @Composable () -> String
) {
    Text(
        text = content(),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(0.9F),
        fontFamily = fontFamily,
        modifier = Modifier.clickable { onClick() }
    )
}