package dev.jordond.dragselectcompose.demo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun AsyncImage(
    url: String,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
)