package dev.jordond.dragselectcompose.demo

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.rememberAsyncImagePainter

@Composable
actual fun AsyncImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier,
) {
    Image(
        painter = rememberAsyncImagePainter(url),
        contentDescription = contentDescription,
        modifier = modifier,
    )
}