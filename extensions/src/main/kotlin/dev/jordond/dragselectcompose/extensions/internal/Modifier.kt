package dev.jordond.dragselectcompose.extensions.internal

import androidx.compose.ui.Modifier

internal fun Modifier.conditional(
    condition: Boolean,
    modifier: Modifier.() -> Modifier,
): Modifier = if (condition) then(modifier(Modifier)) else this