package dev.jordond.dragselectcompose.demo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url

@Composable
actual fun AsyncImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier,
) {
    KamelImage(
        resource = asyncPainterResource(Url(url)),
        contentDescription = contentDescription,
        modifier = modifier,
    )
}