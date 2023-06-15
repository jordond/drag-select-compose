package dev.jordond.dragselectcompose

import androidx.compose.runtime.Composable
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp

public object GridDragSelectDefaults {

    public val hapticsFeedback: HapticFeedback
        @Composable get() = LocalHapticFeedback.current

    public val autoScrollThreshold: Float
        @Composable get() = with(LocalDensity.current) { DEFAULT_THRESHOLD_DP.dp.toPx() }

    private const val DEFAULT_THRESHOLD_DP = 40
}

public data class GridDragSelectState(
    public val selected: Int,
)